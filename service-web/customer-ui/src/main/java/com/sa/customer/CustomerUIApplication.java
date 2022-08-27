package com.sa.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xujin
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients({"com.sa.customer.api.business", "com.sa.product.api.business"})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false, exposeProxy = true)
public class CustomerUIApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerUIApplication.class, args);
    }
}
