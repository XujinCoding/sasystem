package com.sa.customer.api.business.job;

import com.sa.customer.api.business.ICustomerService;
import com.sa.customer.mapper.jpa.BatchTaskItemRepository;
import com.sa.customer.mapper.jpa.BatchTaskRepository;
import com.sa.customer.domain.Customer;
import com.sa.common.dto.job.Type;
import com.sa.customer.domain.BatchTask;
import com.sa.customer.domain.BatchTaskItem;
import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.TaskLevel;
import com.sa.common.utils.OrikaMapperUtils;
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
 *     "type":"1",
 *     "data":"客戶11,123,3 客戶12,123,3 客戶13,123,3 客戶14,123,3 客戶15,123,3 客戶16,123,3 客戶17,123,3 "
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

    public void addCustomerJob(){
//        将任务拆分到任务细节表
        doTaskItem();


//        更新任务状态和成功失败数
        updateTaskStatusAndNums();

    }



    public void updateTaskStatusAndNums() {
        //查询所有的正在处理中的任务
        List<BatchTask> taskList = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.RUNNING);
        taskList.forEach( task ->{
            RLock fairLock = redissonClient.getFairLock(priKey +"_CHANGE_STATUS:"+ task.getTaskId());
            if (tryLock(fairLock,20, TimeUnit.SECONDS)) {//是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
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
            }
        });
    }
    public void doTaskItem() {
        //查询所有的未处理的customer任务, 并且按照级别进行排序
        List<BatchTask> taskLists = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.PREPARING);
        //将数据转换成DTO
        List<BatchTaskDTO> taskList = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(taskLists, BatchTaskDTO.class);
        //分别解析每一个任务, 并将任务放到细节表中
        taskList.forEach(task->{
            //使用Redisson进行加锁
            RLock fairLock = redissonClient.getFairLock(priKey+":"+ task.getTaskId());
            if (tryLock(fairLock,20, TimeUnit.SECONDS)){//是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
                log.info("------------------加锁");
                //如果任务的数据为null,不用进行分解,直接将任务设置成处理成功即可
                if (Objects.isNull(task.getData())){
                    batchTaskRepository.changeTaskStatus(task.getTaskId(),Status.SUCCESS.ordinal());
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (task.getType()== Type.BATCH_ADD_CUSTOMER){
                    //解析字符串分析出每一个任务,并将任务放到细节表中进行处理
                    Integer total = parseStringToTask(task);


                    //设置总数为total
                    batchTaskRepository.setTaskTotal(task.getTaskId(),total);

                    //设置当前任务为进行中
                    batchTaskRepository.changeTaskStatus(task.getTaskId(),Status.RUNNING.ordinal());
                }
                // 操作完成释放掉锁
                fairLock.unlock();
                log.info("------------------解锁");
            }
        });
    }

    private void replaceTaskLevel() {
        List<BatchTask> taskList = batchTaskRepository.getByStateOrderByTaskLevelDescTaskIdAsc(Status.PREPARING);
        taskList.forEach((s)->{
            TaskLevel taskLevel = s.getTaskLevel();
            Long taskId = s.getTaskId();
            //判断这个任务ID 是否在任务细节表中存在, 如果存在说明没完全分解成功, 就将这个任务的优先级提高为HEIGHT
            List<BatchTask> batchTasks = batchTaskRepository.findByTaskId(taskId);
            if (batchTasks.size()!=0){
                //满足这个条件就说明任务已经进行拆分, 但是还没有拆解完成, 需要继续拆解,所以将任务的优先级提高
                batchTaskRepository.changeTaskLevelByTaskId(taskId,TaskLevel.HEIGHT);
            }
        });
    }

    public Integer parseStringToTask(BatchTaskDTO batchTaskDTO) {
        String data = batchTaskDTO.getData();
        int i = 0;
        //如果传进来的数据是空串就返回null
        if (StringUtils.isEmpty(data)){
            return 0;
        }
        //按照" " 进行拆分,拆分成每一个客户
        String[] tasksString= data.split(" ");
        //遍历每一个客户
        for (String task : tasksString) {
            //如果拆分成空串 则放弃这个解析
            //"aaa  bbb c"这种类型的字符串就会拆分成[aaa, , bbb, c]
            if (StringUtils.isEmpty(task)){
                continue;
            }
            i++;
            //去掉两端的空格
            task = task.trim();

            String[] taskElement = task.split(",");
            if (
                    taskElement.length!=3 ||
                            //1-10位中文数字字母的组合
                            !Pattern.matches("[\\u4e00-\\u9fa5,a-z,1-9]{1,10}",taskElement[0])||
                            //三位以内的数字
                            !Pattern.matches("^[1-9]\\d{0,2}|0$",taskElement[1])||
                            //1-100位中文数字字母的组合
                            !Pattern.matches("[\\u4e00-\\u9fa5,a-z,1-9]{1,100}",taskElement[2])
            ){
                //验证失败,
                BatchTaskItem detail = new BatchTaskItem();
                detail.setTaskId(batchTaskDTO.getTaskId());
                detail.setState(Status.FAILURE);
                detail.setMsg("数据解析失败,数据为: "+task);
                insertItemTable(detail);
                continue;
            }
            //通过验证之后
            Customer customer = new Customer(-1L,taskElement[0],Integer.parseInt(taskElement[1]),taskElement[2]);
            //构建明细对象, 插入数据库
            insertItemTOBatchTaskItem(batchTaskDTO, customer);

        }
        return i;
    }
    int k = 0;
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
        if (byCustomerAgeAndCustomerNameAndCustomerHome.size()==0)
            insertItemTable(detail);
        k++;
        if (k==5){
            throw new RuntimeException("测试异常");
        }
    }

    public void insertItemTable(BatchTaskItem detail) {
        BatchTaskItem save = batchTaskItemRepository.save(detail);
        log.info("------save = "+save);
    }

    public Boolean tryLock(RLock rLock, long leaseTime, TimeUnit unit) {
        boolean tryLock = false;
        try {
            tryLock = rLock.tryLock(0, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
        return tryLock;
    }
}
