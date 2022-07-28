package com.sa;

import com.sa.config.JerseyServiceAutoScanner;
import org.glassfish.jersey.server.ResourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.sa")
@MapperScan("com.sa.mapper.mybaits")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
//@EnableFeignClients
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
    @Bean
    public ResourceConfig jerseyConfig(ApplicationContext applicationContext){
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.registerClasses(JerseyServiceAutoScanner.getPublishJerseyServiceClasses(applicationContext,"com.sa.product.api.business"));
        return resourceConfig;
    }

}
