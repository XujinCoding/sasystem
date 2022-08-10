package com.sa.customer.api.business.job;

import com.sa.customer.api.business.ICustomerService;
import com.sa.dto.job.BatchTaskDTO;
import com.sa.dto.job.Operate;
import com.sa.domain.BatchTaskItem;
import com.sa.domain.Customer;
import com.sa.mapper.customer.jpa.BatchTaskItemRepository;
import com.sa.mapper.customer.jpa.BatchTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 本类用于拆分任务细节, 并且更新状态
 */
@Component
@Slf4j
//忽略所有警告
@SuppressWarnings("all")
public class BatchAcceptCustomerJob {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private BatchTaskItemRepository batchTaskItemRepository;

    @Autowired
    private BatchTaskRepository batchTaskRepository;



    @Scheduled(fixedDelay = 10000)
    public void addCustomerJob(){
        //将任务拆分到任务细节表
        doTaskItem();

        //更新任务状态和成功失败数
        updateTaskStatusAndNums();
    }

    private void updateTaskStatusAndNums() {
        //查询所有的正在处理中的任务
        List<BatchTaskDTO> taskList = customerService.getTaskByStatus(2);
        taskList.forEach( task ->{
            Long taskId = task.getTaskId();
            Integer success = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(1, taskId);
            Integer fail = batchTaskItemRepository.countBatchTaskItemByStateAndTaskId(-1, taskId);
            batchTaskRepository.setSuccessAndFailNum(taskId,success,fail);
            Integer total = batchTaskRepository.findTotalByTaskId(taskId);
            //如果成功数 + 失败数 = 总数 ,代表所有数据处理成功, 将任务设置为已完成
            if (success+fail>= total){
                batchTaskRepository.changeTaskStatus(taskId,1);
                //将当前任务的data设置为null
                batchTaskRepository.setDataOkByTaskId(taskId);
            }
        });
    }

    private void doTaskItem() {
        //查询所有的未处理的customer任务
        List<BatchTaskDTO> taskList = customerService.getTaskByStatus(0);

        //分别解析每一个任务, 并将任务放到细节表中
        taskList.forEach(task->{
            //如果任务的数据为null,不用进行分解,直接将任务设置成处理成功即可
            if (Objects.isNull(task.getData())){
                batchTaskRepository.changeTaskStatus(task.getTaskId(),1);
            }
            if (task.getOperate()== Operate.BATCH_ADD_CUSTOMER){
                //解析字符串分析出每一个任务,并将任务放到细节表中进行处理
                Integer total = parseStringToTask(task);

                //设置总数为total
                batchTaskRepository.setTaskTotal(task.getTaskId(),total);

                //设置当前任务为进行中
                batchTaskRepository.changeTaskStatus(task.getTaskId(),2);
            }


        });
    }

    private void insertItemTable(BatchTaskItem detail) {
        BatchTaskItem save = batchTaskItemRepository.save(detail);
        log.info("------save = "+save);

    }


    private Integer parseStringToTask(BatchTaskDTO batchTaskDTO) {
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
                //验证失败, 将任务字符串放到失败任务队列中
                BatchTaskItem detail = new BatchTaskItem();
                detail.setTaskId(batchTaskDTO.getTaskId());
                detail.setState(-1);
                detail.setMsg("数据解析失败,数据为: "+task);
                insertItemTable(detail);
                continue;
            }
            //通过验证之后
            Customer customer = new Customer(-1L,taskElement[0],Integer.parseInt(taskElement[1]),taskElement[2]);
            insertItemTOBatchTaskItem(batchTaskDTO, customer);
        }
        return i;
    }

    private void insertItemTOBatchTaskItem(BatchTaskDTO batchTaskDTO, Customer customer) {
        BatchTaskItem detail = new BatchTaskItem();
        detail.setTaskId(batchTaskDTO.getTaskId());
        detail.setState(0);
        detail.setCustomerAge(customer.getCustomerAge());
        detail.setCustomerHome(customer.getCustomerHome());
        detail.setCustomerName(customer.getCustomerName());
        insertItemTable(detail);
    }
}
