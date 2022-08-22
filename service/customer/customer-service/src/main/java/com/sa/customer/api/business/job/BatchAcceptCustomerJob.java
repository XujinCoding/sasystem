package com.sa.customer.api.business.job;

import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.Type;
import com.sa.customer.api.business.ICustomerService;
import com.sa.customer.domain.BatchTask;
import com.sa.customer.domain.BatchTaskItem;
import com.sa.customer.dao.jpa.BatchTaskItemRepository;
import com.sa.customer.dao.jpa.BatchTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 本类用于拆分任务细节, 并且更新状态
 * {
 * "type":"1",
 * "data":"顾客1 ,顾客2 ,顾客3 ,顾客4 ,顾客5 ,顾客6 ,顾客7 ,顾客8 ,顾客9 ,顾客10 ,顾客11 ,顾客12 ,顾客13 ,顾客14 ,顾客15 ,顾客16 ,顾客17 ,顾客18 ,顾客19 ,顾客20 ,顾客21 ,顾客22 ,顾客23 ,顾客24 ,顾客25 ,顾客26 ,顾客27 ,顾客28 ,顾客29 ,顾客30 ,顾客31 ,顾客32 ,顾客33 ,顾客34 ,顾客35 ,顾客36 ,顾客37 ,顾客38 ,顾客39 ,顾客40 ,顾客41 ,顾客42 ,顾客43 ,顾客44 ,顾客45 ,顾客46 ,顾客47 ,顾客48 ,顾客49 ,顾客50 ,顾客51 ,顾客52 ,顾客53 ,顾客54 ,顾客55 ,顾客56 ,顾客57 ,顾客58 ,顾客59 ,顾客60 ,顾客61 ,顾客62 ,顾客63 ,顾客64 ,顾客65 ,顾客66 ,顾客67 ,顾客68 ,顾客69 ,顾客70 ,顾客71 ,顾客72 ,顾客73 ,顾客74 ,顾客75 ,顾客76 ,顾客77 ,顾客78 ,顾客79 ,顾客80 ,顾客81 ,顾客82 ,顾客83 ,顾客84 ,顾客85 ,顾客86 ,顾客87 ,顾客88 ,顾客89 ,顾客90 ,顾客91 ,顾客92 ,顾客93 ,顾客94 ,顾客95 ,顾客96 ,顾客97 ,顾客98 ,顾客99 ,顾客100"
 * }
 */
@Component
@Slf4j
//忽略所有警告
@SuppressWarnings("all")
public class BatchAcceptCustomerJob {
    private static final String priKey = "BATCH_ACCEPT_CUSTOMER";

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private BatchTaskItemRepository batchTaskItemRepository;

    @Autowired
    private BatchTaskRepository batchTaskRepository;


    @Scheduled(fixedDelay = 10000)
    public void addCustomerJob() {
//        将任务拆分到任务细节表
        doTaskItem();

//        更新任务状态和成功失败数
        updateTaskStatusAndNums();
    }


    public void updateTaskStatusAndNums() {
        //查询所有的正在处理中的任务
        List<BatchTask> taskList = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.RUNNING);
        taskList.forEach(task -> {
            try (RedisFairLock redisFairLock = new RedisFairLock(priKey + "_CHANGE_STATUS:" + task.getTaskId())) {
                //是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
                if (tryLock(redisFairLock.getFairLock(), TimeUnit.SECONDS)) {
                    log.info("-----------------updateTaskStatusAndNums加锁");
                    Long taskId = task.getTaskId();
                    Integer success = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(Status.SUCCESS, taskId);
                    Integer fail = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(Status.FAILURE, taskId);
                    Integer total = batchTaskRepository.findTotalByTaskId(taskId);
                    //如果成功数 + 失败数 = 总数 ,代表所有数据处理成功, 将任务设置为已完成
                    if (success + fail >= total) {
                        task.setFailNum(fail).setSuccessNum(success).setData("ok").setState(Status.SUCCESS);
                        batchTaskRepository.save(task);
                    } else {
                        batchTaskRepository.setSuccessAndFailNum(taskId, success, fail);
                    }
                }
            } catch (IOException e) {
                log.error("BatchAcceptCustomerJob:updateTaskStatusAndNums------------", e);
            }
        });
    }

    public void doTaskItem() {
        //查询所有的未处理的customer任务, 并且按照级别进行排序
        List<BatchTask> taskLists = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.PREPARING);
        //分别解析每一个任务, 并将任务放到细节表中
        taskLists.forEach(task -> {
            if (Objects.isNull(task.getData())) {
                //由于没有数据所以将total数据置为0
                batchTaskRepository.modifyTaskStatusAndTotal(task.getTaskId(), Status.SUCCESS.ordinal(), 0);
                return;
            }
            //使用Redisson进行加锁
            try (RedisFairLock redisFairLock = new RedisFairLock(priKey + ":" + task.getTaskId())) {
                //是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
                if (tryLock(redisFairLock.getFairLock(), TimeUnit.SECONDS)) {
                    log.info("-----------------doTaskItem加锁");
                    //如果任务的数据为null,不用进行分解,直接将任务设置成处理成功即
                    if (Type.BATCH_ADD_CUSTOMER.equals(task.getType())) {
                        //设置总数并且修改任务状态
                        batchTaskRepository.modifyTaskStatusAndTotal(
                                task.getTaskId(),
                                Status.RUNNING.ordinal(),
                                //解析字符串分析出每一个任务,并将任务放到细节表中进行处理
                                parseStringToTask(task));
                    }
                }
            } catch (IOException e) {
                log.error("BatchAcceptCustomerJob:doTaskItem------------", e);
            }
        });
    }

    public Integer parseStringToTask(BatchTask batchTask) {
        String data = batchTask.getData();
        Long taskId = batchTask.getTaskId();
        List<BatchTaskItem> allList = batchTaskItemRepository.findAllByTaskId(taskId);
        //如果传进来的数据是空串就返回null
        if (StringUtils.isEmpty(data)) {
            return 0;
        }
        String[] customerArray = data.split(",");
        List<BatchTaskItem> list = Arrays.stream(customerArray)
                .filter((cusName) -> !allList.contains(cusName))
                .map(c -> new BatchTaskItem(taskId, Status.PREPARING, "化简逻辑", c, 22, "北京"))
                .collect(Collectors.toList());
        insertItemTOBatchTaskItem(list);
        return customerArray.length;
    }


    public void insertItemTOBatchTaskItem(List<BatchTaskItem> taskItemList) {
        //传递进来的都是不在明细表中的数据, 可以直接入库
        taskItemList.forEach(item -> {
            insertItemTable(item);
        });
    }

    public void insertItemTable(BatchTaskItem detail) {
        BatchTaskItem save = batchTaskItemRepository.save(detail);
        log.info("------save = " + save);
    }

    public Boolean tryLock(RLock rLock, TimeUnit unit) {
        boolean tryLock = false;
        try {
            tryLock = rLock.tryLock(0, -1, unit);
        } catch (InterruptedException e) {
            log.error("", e);
            return false;
        }
        return tryLock;
    }
}