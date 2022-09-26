package com.sa.customer.api.business.job;

import com.sa.common.lock.Lock;
import com.sa.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author starttimesxj
 * 如果不指定时间单位和长度, 默认开启续命锁
 */
@Slf4j
public class RedisFairLock implements Lock {

    public RedissonClient redissonClient = SpringUtil.getBean("redissonClient", RedissonClient.class);

    private final RLock fairLock;

    private TimeUnit unit = TimeUnit.SECONDS;

    private int time = -1;

    private String key = "";


    public RedisFairLock(String lockKey) {
        fairLock = redissonClient.getFairLock(lockKey);
        this.key = lockKey;

    }

    public RedisFairLock(String lockKey, TimeUnit unit, int time) {
        fairLock = redissonClient.getFairLock(lockKey);
        this.unit = unit;
        this.time = time;
        this.key = lockKey;
    }

    @Override
    public boolean tryLock() {
        boolean tryLock = false;
        try {
            tryLock = fairLock.tryLock(0, time, unit);
        } catch (InterruptedException e) {
            log.error("BatchAcceptCustomerJob:tryLock------------", e);
            return false;
        }
        return tryLock;
    }

    @Override
    public void unlock() {
        fairLock.unlock();
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(fairLock)) {
            //自动关闭锁
            try {
                unlock();
            } catch (Exception e) {
                log.error("RedisFairLock : close()", e);
            }
            log.info("-----------------自动解锁 " + key);

        }
    }

    public String getkey() {
        return key;
    }
}
