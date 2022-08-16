package com.sa.customer;

import com.sa.customer.config.JerseyServiceAutoScanner;
import com.sa.common.converter.EnumParamConverter;
import org.glassfish.jersey.server.ResourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.sa.customer.mapper.mybatis")
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class,args);
    }
    @Bean
    public ResourceConfig jerseyConfig(ApplicationContext applicationContext){
        ResourceConfig resourceConfig = new ResourceConfig();
        //注册jersey注解扫描器
        resourceConfig.registerClasses(JerseyServiceAutoScanner.getPublishJerseyServiceClasses(applicationContext,"com.sa.customer.api.business"));
        /*
         * 用于进行枚举参数在传递的时候,使用@EnumFormat注解进行标记之后转换
         */
        resourceConfig.register(EnumParamConverter.class);
        return resourceConfig;
    }
}

