package com.sa.customer.api.business.job;

import com.sa.domain.BatchTask;
import com.sa.domain.BatchTaskItem;
import com.sa.dto.job.Status;
import com.sa.dto.job.Type;
import com.sa.mapper.mybaits.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 本类只去解析批量任务, 将批量任务分解到明细表, 还会进行状态的判断
 */
@Component
@Slf4j
public class BatchAcceptTaskProductJob {
    @Autowired
    private ProductMapper productMapper;

//    private static BloomFilter<ProductDTO> bloomFilter = BloomFilter.create(Funnels.integerFunnel())




    /**
     * 创建定时任务, 去任务库中拿到任务,并且解析成细节
     */
    @Scheduled(fixedDelay = 10000)
    public void parseProductTask(){

        //解析任务
        doTaskItem();

        //修改进行中的任务的状态
        updateTaskStatusAndNums();
    }



    private void doTaskItem() {
        //查询所有的未处理的任务
        List<BatchTask> tasksListNon = productMapper.getNotRunningTask();
        //这里不需要对tasksListNon 进行判空, 使用forEach进行遍历的时候, listSize 为空的时候不会去遍历, 也不报错
        //这一步只去进行拆分操作, 并且将数据插入到明细表中

        tasksListNon.forEach((task)->{
            if (task.getType() == Type.BATCH_ADD_PRODUCT)
                parseBatchAddProductTask(task);
        });
    }

    //从任务列表中取出任务, 将任务入库
    private void parseBatchAddProductTask(BatchTask task) {
        //将字符串进行解析并且入库
        Integer size = parseString(task);

        //设置大小
        productMapper.setTaskTotal(task.getTaskId() ,size);

        //将当前的任务在任务列表中设置成进行中
        productMapper.changeTaskTableState(task.getTaskId(), Status.RUNNING);

    }

    private void updateTaskStatusAndNums() {
        //查询处理中的任务清单
        List<BatchTask> tasksListIng = productMapper.getRunningTask();
        tasksListIng.forEach((task) -> {
            Long taskId = task.getTaskId();
            // 判断上一次执行的任务是否执行完毕, 执行完毕之后就把上一次执行的任务设置成处理成功
            Integer successNum = productMapper.getSuccessNumber(taskId);
            productMapper.setSuccessNumber(successNum, taskId);
            Integer failNum = productMapper.getFailNumber(taskId);
            productMapper.setFailNumber(failNum, taskId);
            Integer totalNum = productMapper.getTotalNumber(taskId);
            if (successNum + failNum >= totalNum) {
                //任务执行成功, 将任务状态设置成完成
                productMapper.setTaskStatus(taskId, Status.SUCCESS);
            }
        });
    }

    /**
     * 解析字符串
     * 字符串生成模式, 每一行是一个商品信息, 信息属性之间使用','隔开, 商品之间有换行符
     */
    public Integer parseString(BatchTask batchTask){
        String data = batchTask.getData();
        if(StringUtils.isEmpty(data)){
            productMapper.setTaskStatus(batchTask.getTaskId(), Status.SUCCESS);
        }
        int i = 0;
        String[] split = data.split(" ");
        for (String s : split) {
            if (StringUtils.isEmpty(s)){
                continue;
            }
            i++;
            String[] split1 = s.split(",");
            if (
                    split1.length!=4 ||
                            StringUtils.isEmpty(split1[0])||
                            !Pattern.matches("^-?\\d+(\\.\\d+)?$",split1[1])||
                            !Pattern.matches("^-?\\d+(\\.\\d+)?$",split1[2])||
                            StringUtils.isEmpty(split1[3])
            ){
//                productMapper.setItemStatus(batchTask.getTaskId(), -1, "数据解析出错:" + batchTask.getData());
                BatchTaskItem batchTaskItem = new BatchTaskItem();
                batchTaskItem.setTaskId(batchTask.getTaskId());
                batchTaskItem.setMsg("数据解析出错:" + batchTask.getData());
                batchTaskItem.setState(Status.FAILURE);
                productMapper.addItem(batchTaskItem);
                continue;
            }
            BatchTaskItem batchTaskItem = new BatchTaskItem();
            batchTaskItem.setTaskId(batchTask.getTaskId());
            batchTaskItem.setProductName(split1[0]);
            batchTaskItem.setProductNum(Integer.parseInt(split1[1]));
            batchTaskItem.setProductPrice(Integer.parseInt(split1[2]));
            batchTaskItem.setProductRemark(split1[3]);
            batchTaskItem.setState(Status.PREPARING);
            batchTaskItem.setMsg("");
            productMapper.addItem(batchTaskItem);
        }
        return i;
    }



}
