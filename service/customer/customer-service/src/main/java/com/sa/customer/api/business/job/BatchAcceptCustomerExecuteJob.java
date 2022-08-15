package com.sa.customer.api.business.job;

import com.sa.domain.BatchTaskItem;
import com.sa.domain.Customer;
import com.sa.dto.job.Status;
import com.sa.dto.job.Type;
import com.sa.mapper.customer.jpa.BatchTaskItemRepository;
import com.sa.mapper.customer.jpa.BatchTaskRepository;
import com.sa.mapper.customer.jpa.CustomerRepository;
import com.sa.mapper.customer.mybatis.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 本类用于执行任务细节, 将任务添加到指定数据库中
 */
@Component
@Slf4j
public class BatchAcceptCustomerExecuteJob {
    private static final String priKey = "BATCH_ACCEPT_CUSTOMER";

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BatchTaskItemRepository batchTaskItemRepository;

    @Autowired
    private BatchTaskRepository batchTaskRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Scheduled(fixedDelay = 100000)
    public void executeTaskItem(){
        List<BatchTaskItem> taskItems = batchTaskItemRepository.findBatchTaskItemsByState(Status.PREPARING);
        taskItems.forEach(item->{
            RLock fairLock = redissonClient.getFairLock(priKey +"_ADD_ITEM_INTO_CUSTOMER:"+ item.getId());
            if (tryLock(fairLock,20, TimeUnit.SECONDS)) {//是否可以获得锁,不能获得锁就不进行操作, 不需要进行等待
                Integer typeNum = batchTaskRepository.findTypeByTaskId(item.getTaskId());
                if (typeNum == Type.BATCH_ADD_CUSTOMER.ordinal()) {
                    try {
                        //将信息插入到Customer表中
                        addItemIntoCustomer(item);

                        //修改信息
                        batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.SUCCESS.ordinal(), "");

                    } catch (Exception e) {
                        String message = e.getMessage();
                        batchTaskItemRepository.setStatusAndMsgById(item.getId(), Status.FAILURE.ordinal(), message);

                    }
                }
            }
        });
    }

    private void addItemIntoCustomer(BatchTaskItem item) {
        Customer customer = new Customer();
        customer.setCustomerAge(item.getCustomerAge());
        customer.setCustomerHome(item.getCustomerHome());
        customer.setCustomerName(item.getCustomerName());
        Customer target =  customerMapper.findByAgeAndName(customer);
        if (Objects.isNull(target)){
            Customer save = customerRepository.save(customer);
            log.info("------------存储到Customer表-----------"+save);
        }else{
            throw new RuntimeException("用户已经存在");
        }
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




