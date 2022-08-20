package com.sa.customer.api.business.job;

import com.sa.customer.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
/**
 * @author starttimesxj
 */
@Slf4j
public class RedisFairLock implements Closeable {



    public RedissonClient redissonClient = SpringUtil.getBean("redissonClient",RedissonClient.class);

    private RLock fairLock;


    public RLock getFairLock(){
        return fairLock;
    }

    public RedisFairLock(String lockKey) {
        fairLock = redissonClient.getFairLock(lockKey);
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(fairLock)){
            //自动关闭锁
            fairLock.unlock();
            log.info("-----------------自动解锁" + fairLock.getName());
        }
    }
}
