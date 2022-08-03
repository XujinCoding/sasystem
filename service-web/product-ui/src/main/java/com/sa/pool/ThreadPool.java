package com.sa.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10L,
                //最大空闲时间的单位
                TimeUnit.SECONDS,
                //等待队列
                new ArrayBlockingQueue(10),
                //线程工厂
                Executors.defaultThreadFactory(),
                //失效策略
                new ThreadPoolExecutor.AbortPolicy());
    }
    private ThreadPool(){
    }
    private static ThreadPoolExecutor threadPool = null;

    private static ThreadPoolExecutor getThreadPool(){
        if (threadPool == null){
            synchronized (ThreadPool.class) {
                if (threadPool == null) {
                    //线程池中基本线程数,最大线程数,最大空闲时间
                    threadPool = new ThreadPoolExecutor(5, 10, 10L,
                            //最大空闲时间的单位
                            TimeUnit.SECONDS,
                            //等待队列
                            new ArrayBlockingQueue(10),
                            //线程工厂
                            Executors.defaultThreadFactory(),
                            //失效策略
                            new ThreadPoolExecutor.AbortPolicy());
                     /*
                        当工作队列满了且同时运行的线程数达到了最大工作线程的时候,
                        1. ThreadPoolExecutor.AbortPolicy(): 直接抛出异常拒绝新的任务
                        2. ThreadPoolExecutor.CallerRunPolicy(): 使用调用者所在的线程执行任务,但是这种策略会降低对于新任务的提交速度
                        3. ThreadPoolExecutor.DiscardPolicy(): 不处理新任务, 直接丢弃掉
                        4. ThreadPoolExecutor.DiscardOldestPolicy(): 丢弃最早未处理的任务
                     */
                }
            }


        }
        return threadPool;
    }
}
