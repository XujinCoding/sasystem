package com.sa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.sa")
@EnableFeignClients({"com.sa.product.api.business"})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = false,exposeProxy = true)
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }



//    @Bean
//    public ResourceConfig jerseyConfig(ApplicationContext applicationContext){
//        ResourceConfig resourceConfig = new ResourceConfig();
//        resourceConfig.registerClasses(JerseyServiceAutoScanner.getPublishJerseyServiceClasses(applicationContext,"com.sa.product.api.business"));
//        return resourceConfig;
//    }
}
