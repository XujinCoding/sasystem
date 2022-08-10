package com.sa;

import com.sa.config.JerseyServiceAutoScanner;
import com.sa.converter.EnumParamConverter;
import org.glassfish.jersey.server.ResourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sa.mapper.customer.mybatis")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
public class CustonerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustonerServiceApplication.class,args);
    }
    @Bean
    public ResourceConfig jerseyConfig(ApplicationContext applicationContext){
        ResourceConfig resourceConfig = new ResourceConfig();
        //注册jesery注解扫描器
        resourceConfig.registerClasses(JerseyServiceAutoScanner.getPublishJerseyServiceClasses(applicationContext,"com.sa.customer.api.business"));
        /**
         * 用于进行枚举参数在传递的时候,使用@EnumFormat注解进行标记之后转换
         */
        resourceConfig.register(EnumParamConverter.class);
        return resourceConfig;
    }
}
