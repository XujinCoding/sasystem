package com.sa.customer.api.business.job;

import com.sa.common.lock.Lock;
import com.sa.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author starttimesxj
 */
@Slf4j
public class RedisFairLock implements Lock {

    public RedissonClient redissonClient = SpringUtil.getBean("redissonClient",RedissonClient.class);

    private final RLock fairLock;

    private final TimeUnit unit;


    public RedisFairLock(String lockKey,TimeUnit unit) {
        fairLock = redissonClient.getFairLock(lockKey);
        this.unit = unit;
    }

    @Override
    public boolean tryLock() {
        boolean tryLock = false;
        try {
            tryLock = fairLock.tryLock(0, -1, unit);
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
        if (Objects.nonNull(fairLock)){
            //自动关闭锁
            try {
                unlock();
            }catch (Exception e) {
                log.error("RedisFairLock : close()",e);
            }
            log.info("-----------------自动解锁" + fairLock.getName());

        }
    }
    private static final Pattern PATTERN = Pattern.compile("\\d\\+\\-\\/]{0,25}");
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();

        List<String> list = new ArrayList<>();
        Random random = new Random();
        for (int i =0 ; i<10000000 ;i++){
            list.add(String.valueOf(random.nextInt(100000)));
        }
        stopWatch.start("string.matches");
        for (int i =0 ; i<10000000 ;i++){
            if (list.get(i).matches("[\\d\\+\\-\\/]{0,25}")) {}
        }
        stopWatch.stop();

        stopWatch.start("PATTERN.matcher");
        for (int i =0 ; i<10000000 ;i++){
            if (PATTERN.matcher(list.get(i)).matches()) {}
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());


    }
}
