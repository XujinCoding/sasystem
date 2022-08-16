package com.sa.customer.api.business.job;

import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.TaskLevel;
import com.sa.common.dto.job.Type;
import com.sa.common.utils.OrikaMapperUtils;
import com.sa.customer.api.business.ICustomerService;
import com.sa.customer.domain.BatchTask;
import com.sa.customer.domain.BatchTaskItem;
import com.sa.customer.domain.Customer;
import com.sa.customer.mapper.jpa.BatchTaskItemRepository;
import com.sa.customer.mapper.jpa.BatchTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 本类用于拆分任务细节, 并且更新状态
 * {
 * "type":"1",
 * "data":"顾客71,123,72 顾客72,123,73 顾客73,123,74 顾客74,123,75 顾客75,123,76 顾客76,123,77 顾客77,123,78 顾客78,123,79 顾客79,123,80 顾客80,123,81 顾客81,123,82 顾客82,123,83 顾客83,123,84 顾客84,123,85 顾客85,123,86 顾客86,123,87 顾客87,123,88 顾客88,123,89 顾客89,123,90 顾客90,123,91 顾客91,123,92 顾客92,123,93 顾客93,123,94 顾客94,123,95 顾客95,123,96 顾客96,123,97 顾客97,123,98 顾客98,123,99 顾客99,123,100 顾客100,123,101 顾客101,123,102 顾客102,123,103 顾客103,123,104 顾客104,123,105 顾客105,123,106 顾客106,123,107 顾客107,123,108 顾客108,123,109 顾客109,123,110 顾客110,123,111 顾客111,123,112 顾客112,123,113 顾客113,123,114 顾客114,123,115 顾客115,123,116 顾客116,123,117 顾客117,123,118 顾客118,123,119 顾客119,123,120 顾客120,123,121 顾客121,123,122 顾客122,123,123 顾客123,123,124 顾客124,123,125 顾客125,123,126 顾客126,123,127 顾客127,123,128 顾客128,123,129 顾客129,123,130 顾客130,123,131 顾客131,123,132 顾客132,123,133 顾客133,123,134 顾客134,123,135 顾客135,123,136 顾客136,123,137 顾客137,123,138 顾客138,123,139 顾客139,123,140 顾客140,123,141 顾客141,123,142 顾客142,123,143 顾客143,123,144 顾客144,123,145 顾客145,123,146 顾客146,123,147 顾客147,123,148 顾客148,123,149 顾客149,123,150 顾客150,123,151 顾客151,123,152 顾客152,123,153 顾客153,123,154 顾客154,123,155 顾客155,123,156 顾客156,123,157 顾客157,123,158 顾客158,123,159 顾客159,123,160 顾客160,123,161 顾客161,123,162 顾客162,123,163 顾客163,123,164 顾客164,123,165 顾客165,123,166 顾客166,123,167 顾客167,123,168 顾客168,123,169 顾客169,123,170 顾客170,123,171   "
 * }
 * <p>
 * {
 * "type":"1",
 * "data":"顾客1,123,3 顾客2,123,4 顾客3,123,5 顾客4,123,6 顾客5,123,7 顾客6,128 顾客7,123,9 顾客8,123,10 顾客9,123,11 顾客10,123,12 顾客11,123,13 顾客12,123,14 顾客13,123,15 顾客14,123,16 顾客15,123,17 顾客16,123,18 顾客17,123,19 顾客18,123,20 顾客19,123,21 顾客,123,22 顾客21,12,23 顾客22,123,24 顾客,123,25 顾客24,123, 顾客25,123,27 顾客26,123,28 顾客27,123,29 顾客28,123,30 顾客29,123,31 顾客30,123,32 顾客31,123,33 顾客32,123,34 顾客33,12,35,123,50  顾客63,123,65 顾客64,12,66 客65,123,67 顾客66,123,68 顾客67,123,69 顾客68,123,70 顾客69,123,71 顾客70,123,72  "
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

    @Autowired
    private RedissonClient redissonClient;

    int i = 0;

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
            RLock fairLock = redissonClient.getFairLock(priKey + "_CHANGE_STATUS:" + task.getTaskId());
            //是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
            if (tryLock(fairLock, TimeUnit.SECONDS)) {
                log.info("-----------------updateTaskStatusAndNums加锁" + fairLock.getName());
                Long taskId = task.getTaskId();
                Integer success = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(Status.SUCCESS, taskId);
                Integer fail = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(Status.FAILURE, taskId);
                batchTaskRepository.setSuccessAndFailNum(taskId, success, fail);
                Integer total = batchTaskRepository.findTotalByTaskId(taskId);
                //如果成功数 + 失败数 = 总数 ,代表所有数据处理成功, 将任务设置为已完成
                if (success + fail >= total) {
                    batchTaskRepository.changeTaskStatus(taskId, Status.SUCCESS.ordinal());

                    //将当前任务的data设置为null
                    batchTaskRepository.setDataOkByTaskId(taskId);
                }
                fairLock.unlock();
                log.info("-----------------updateTaskStatusAndNums解锁" + fairLock.getName());
            }
        });
    }

    public void doTaskItem() {
        //查询所有的未处理的customer任务, 并且按照级别进行排序
        List<BatchTask> taskLists = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.PREPARING);
        //将数据转换成DTO
        List<BatchTaskDTO> taskList = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(taskLists, BatchTaskDTO.class);
        //分别解析每一个任务, 并将任务放到细节表中
        taskList.forEach(task -> {
            //使用Redisson进行加锁
            RLock fairLock = redissonClient.getLock(priKey + ":" + task.getTaskId());
            //是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
            if (tryLock(fairLock, TimeUnit.SECONDS)) {
                log.info("------------------doTaskItem加锁" + fairLock.getName());
                //如果任务的数据为null,不用进行分解,直接将任务设置成处理成功即可
                if (Objects.isNull(task.getData())) {
                    batchTaskRepository.changeTaskStatus(task.getTaskId(), Status.SUCCESS.ordinal());
                    return;
                }
                if (Type.BATCH_ADD_CUSTOMER.equals(task.getType())) {
                    //解析字符串分析出每一个任务,并将任务放到细节表中进行处理
                    Integer total = parseStringToTask(task);


                    //设置总数为total
                    batchTaskRepository.setTaskTotal(task.getTaskId(), total);

                    //设置当前任务为进行中
                    batchTaskRepository.changeTaskStatus(task.getTaskId(), Status.RUNNING.ordinal());

                    // 操作完成释放掉锁
                    fairLock.unlock();
                    log.info("------------------doTaskItem解锁" + fairLock.getName());
                }
            }
        });
    }

    private void replaceTaskLevel() {
        List<BatchTask> taskList = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.PREPARING);
        taskList.forEach((s) -> {
            TaskLevel taskLevel = s.getTaskLevel();
            Long taskId = s.getTaskId();
            //判断这个任务ID 是否在任务细节表中存在, 如果存在说明没完全分解成功, 就将这个任务的优先级提高为HEIGHT
            List<BatchTask> batchTasks = batchTaskRepository.findByTaskId(taskId);
            if (batchTasks.size() != 0) {
                //满足这个条件就说明任务已经进行拆分, 但是还没有拆解完成, 需要继续拆解,所以将任务的优先级提高
                batchTaskRepository.changeTaskLevelByTaskId(taskId, TaskLevel.HEIGHT);
            }
        });
    }

    public Integer parseStringToTask(BatchTaskDTO batchTaskDTO) {
        String data = batchTaskDTO.getData();
        int i = 0;
        //如果传进来的数据是空串就返回null
        if (StringUtils.isEmpty(data)) {
            return 0;
        }
        //按照" " 进行拆分,拆分成每一个客户
        String[] tasksString = data.split(" ");
        //遍历每一个客户
        for (String task : tasksString) {
            //如果拆分成空串 则放弃这个解析
            //"aaa  bbb c"这种类型的字符串就会拆分成[aaa, , bbb, c]
            if (StringUtils.isEmpty(task)) {
                continue;
            }

            //去掉两端的空格
            task = task.trim();

            String[] taskElement = task.split(",");
            if (
                    taskElement.length != 3 ||
                            //1-10位中文数字字母的组合
                            !Pattern.matches("[\\u4e00-\\u9fa5,a-z,0-9]{1,10}", taskElement[0]) ||
                            //三位以内的数字
                            !Pattern.matches("^[1-9]\\d{0,2}|0$", taskElement[1]) ||
                            //1-100位中文数字字母的组合
                            !Pattern.matches("[\\u4e00-\\u9fa5,a-z,0-9]{1,100}", taskElement[2])
            ) {
                i++;
                //验证失败,
                BatchTaskItem detail = new BatchTaskItem();
                detail.setTaskId(batchTaskDTO.getTaskId());
                detail.setState(Status.FAILURE);
                detail.setMsg("数据解析失败,数据为: " + task);
                insertItemTable(detail);
                continue;
            }
            i++;
            //通过验证之后
            Customer customer = new Customer(-1L, taskElement[0], Integer.parseInt(taskElement[1]), taskElement[2]);
            //构建明细对象, 插入数据库
            insertItemTOBatchTaskItem(batchTaskDTO, customer);
        }
        return i;
    }

    public void insertItemTOBatchTaskItem(BatchTaskDTO batchTaskDTO, Customer customer) {
        Integer customerAge = customer.getCustomerAge();
        String customerName = customer.getCustomerName();
        String customerHome = customer.getCustomerHome();

        BatchTaskItem detail = new BatchTaskItem();
        detail.setTaskId(batchTaskDTO.getTaskId());
        detail.setState(Status.PREPARING);
        detail.setCustomerAge(customerAge);
        detail.setCustomerHome(customerHome);
        detail.setCustomerName(customerName);
        //判断这个明细在任务表中是否存在, 如果存在则不需要入库, 如果不存在就入库
        List<BatchTaskItem> byCustomerAgeAndCustomerNameAndCustomerHome = batchTaskItemRepository.getByCustomerAgeAndCustomerNameAndCustomerHome(customerAge, customerName, customerHome);
        if (byCustomerAgeAndCustomerNameAndCustomerHome.size() == 0)
            insertItemTable(detail);
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
            return false;
        }
        return tryLock;
    }
}
