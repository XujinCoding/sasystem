package com.sa.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients({"com.sa.product.api.business"})
//开启全局的Scheduling扫描机制
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
public class ProductUIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductUIApplication.class,args);
    }

}


