package com.sa.customer.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        Config c = new Config();
//        c.useSingleServer().setAddress("redis://127.0.0.1:6973");
        //c.setLockWatchdogTimeout()
        c.useSingleServer().setAddress("redis://192.168.20.56:6379");
        return Redisson.create(c);
    }

}