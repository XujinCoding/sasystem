package com.sa.product.redis;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RedisTest {

    @Test
    public void test1(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Config config = new Config();
                    config.useSingleServer().setAddress("redis://127.0.0.1:6973");
                    RedissonClient redissonClient = Redisson.create(config);
                    RLock fairLock = redissonClient.getFairLock("xujin");
                    fairLock.lock(2, TimeUnit.SECONDS);
                    System.out.println("------"+Thread.currentThread().getName()+"-----"+new Random().nextInt());
                    fairLock.unlock();
                }
            });

        }




    }

}
