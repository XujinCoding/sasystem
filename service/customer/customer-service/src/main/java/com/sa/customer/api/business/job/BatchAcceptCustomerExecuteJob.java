package com.sa.customer.api.business.job;

import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.Type;
import com.sa.customer.dao.jpa.BatchTaskItemRepository;
import com.sa.customer.dao.jpa.BatchTaskRepository;
import com.sa.customer.dao.jpa.CustomerRepository;
import com.sa.customer.domain.BatchTaskItem;
import com.sa.customer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 本类用于执行任务细节, 将任务添加到指定数据库中
 *
 * @author xujin
 */
@Component
@Slf4j
public class BatchAcceptCustomerExecuteJob {
    private static final String PRI_KEY = "BATCH_ACCEPT_CUSTOMER";


    @Autowired
    private BatchTaskItemRepository batchTaskItemRepository;

    @Autowired
    private BatchTaskRepository batchTaskRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PlatformTransactionManager txManager;

    @Scheduled(fixedDelay = 10000)
    public void executeTaskItem() {
        List<Long> taskIdList = batchTaskRepository.findTasIdByState(Status.RUNNING.ordinal());
        taskIdList.forEach(taskId -> {
            try (RedisFairLock redisFairLock = new RedisFairLock(PRI_KEY + "_ADD_ITEM_INTO_CUSTOMER:" + taskId)) {
                //是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
                if (tryLock(redisFairLock.getFairLock(), TimeUnit.SECONDS)) {
                    List<BatchTaskItem> itemList = batchTaskItemRepository.findBatchTaskItemsByStateAndTaskIdOrderById(Status.PREPARING, taskId);
                    itemList.forEach((item) -> {
                        if (batchTaskRepository.findTypeByTaskId(item.getTaskId()) == Type.BATCH_ADD_CUSTOMER.ordinal()) {
                            if (addItemIntoCustomer(item)) {
                                //修改信息
                                batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.SUCCESS.ordinal(), "");
                            } else {
                                batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.FAILURE.ordinal(), "用户信息存在");
                            }
                        }
                    });
                }
            } catch (IOException e) {
                log.error("BatchAcceptCustomerExecuteJob:executeTaskItem------------", e);
            }
        });
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)

    public Boolean addItemIntoCustomer(BatchTaskItem item) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            Customer customer = new Customer();
            customer.setCustomerAge(item.getCustomerAge());
            customer.setCustomerHome(item.getCustomerHome());
            customer.setCustomerName(item.getCustomerName());
            Customer target = customerRepository.findByCustomerName(item.getCustomerName());
            Customer customer1 = Objects.isNull(target) ? customerRepository.save(customer) : null;
//            int i = 1/0;
            if (Objects.nonNull(customer1)) {
                log.info("------------存储到Customer表-----------" + customer1);
                txManager.commit(status);
                return true;
            }else {
                txManager.commit(status);
                return false;
            }

        } catch (Exception e) {
            //...
            txManager.rollback(status);
            log.error("BatchAcceptCustomerExcuteJob : addItemIntoCustomer",e);
            return false;
        }
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




