package com.sa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.sa")
//TODO ---------
@EnableFeignClients({"com.sa.product.api.business"})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
public class ProductUIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductUIApplication.class,args);
    }
}
