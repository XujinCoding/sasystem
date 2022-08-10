package com.sa.schedule;

import org.springframework.stereotype.Component;

@Component
public class Test {
//    //上一次任务执行完成之后 x 秒才执行, 这个需要等待上一个任务完成1秒之后才执行
//    @Scheduled( fixedDelay = 1000)
//    public void scheduled(){
//        System.out.println("fixedDelay定时任务开启");
//    }
//    //上一次任务开始执行之后 x 秒才执行, 这个是真正意义上的 1秒执行一次
//    @Scheduled( fixedRate  = 1000)
//    public void scheduled1(){
//        System.out.println("fixedRate定时任务开启");
//    }
//    //第一次执行的时候延迟1秒,之后按照fixedDelay 的规则执行
//    @Scheduled(initialDelay = 5000,fixedDelay = 1000)
//    public void scheduled2(){
//        System.out.println("延时5秒后开始");
//    }
//
//    /**
//     * "0 3 8 ? * *" 表示每天上午8点03分执行任务
//     */
//    @Scheduled( cron = "")
//    public void scheduled3(){
//        System.out.println("cron 的任务");
//    }
}
