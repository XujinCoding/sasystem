package com.sa.customer.api.business.job;

import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.Type;
import com.sa.customer.domain.BatchTaskItem;
import com.sa.customer.domain.Customer;
import com.sa.customer.dao.jpa.BatchTaskItemRepository;
import com.sa.customer.dao.jpa.BatchTaskRepository;
import com.sa.customer.dao.jpa.CustomerRepository;
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
                            //手动开启事务
                            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                            TransactionStatus status = txManager.getTransaction(def);
                            try {
                                //将信息插入到Customer表中
                                addItemIntoCustomer(item);
                                //修改信息
                                batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.SUCCESS.ordinal(), "");
                                //提交任务
                                txManager.commit(status);
                            } catch (Exception e) {
                                txManager.rollback(status);
                                batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.FAILURE.ordinal(), e.getMessage());
                            }
                        }
                    });

                }
            } catch (IOException e) {
                log.error("BatchAcceptCustomerExecuteJob:executeTaskItem------------", e);
            }
        });
    }

    private void addItemIntoCustomer(BatchTaskItem item) {
        Customer customer = new Customer();
        customer.setCustomerAge(item.getCustomerAge());
        customer.setCustomerHome(item.getCustomerHome());
        customer.setCustomerName(item.getCustomerName());
        Customer target = customerRepository.findByCustomerName(item.getCustomerName());
        if (Objects.isNull(target)) {
            Customer save = customerRepository.save(customer);
            log.info("------------存储到Customer表-----------" + save);
        } else {
            throw new RuntimeException("用户已经存在");
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




