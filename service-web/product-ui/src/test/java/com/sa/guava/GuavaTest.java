package com.sa.guava;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
@Slf4j
public class GuavaTest {
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        //这里就是创建一个移除监听, 哪个<K,V>被移除了,就调用一下这个方法
        RemovalListener<Long, stu> removalListener = new RemovalListener<>() {
            @Override
            public void onRemoval(RemovalNotification<Long, stu> notification) {
                System.out.println(notification.getKey() + ":" + notification.getValue());
            }
        };
        LoadingCache<Long, stu> loddingPut= CacheBuilder.newBuilder()
                //初始容量: 应该设置一个合适的大小, 由于Guava的缓存使用了分段锁机制,所以扩容的代价非常昂贵
                .initialCapacity(100)
                //最大缓存数量: 缓存数目可能在逼近极限值的时候,或者到达极限值的时候进行内存回收, 会将最近没有使用或者总体上很少使用的缓存项进行回收
                .maximumSize(1000)
                //设置缓存的并发级别为CPU核心数,就是同一时刻的并发数, 默认为4, 一般设置为CPU的核心数量
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                //过期策略: 当数据到达指定时间没有被读或者写, 这个数据将会被作为过期数据进行处理, 当没有数据或者读到过期数据的时候只允许一个线程去更新数据,其他线程阻塞,等待更新完成在获取新的值
                .expireAfterAccess(2, TimeUnit.SECONDS)
                //过期策略: 当数据达到指定时间没有被写, 这个数据就会被作为过期数据进行处理, 当没有数据或者读到过期的数据的时候只允许一个线程去更新数据,其他线程阻塞,等待更新完成在获取新的值
//                .expireAfterWrite(30, TimeUnit.SECONDS)
                //过期策略: 数据在指定时间没有被更新, 则为过期数据, 当有线程正在更新数据的时候, 访问的线程返回旧数据
//                .refreshAfterWrite();
                .removalListener(removalListener)
                .build(new CacheLoader<Long, stu>() {
                    @Override
                    public stu load(Long key) throws Exception {
                        return new stu(key,"100");
                    }
                });

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"---获取数据");
                loddingPut.get(1L);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
            try {
                loddingPut.get(1L);
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName()+"---获取数据");
                loddingPut.get(1L);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(10000000);
    }

}
class stu{
    private Long Id;
    private String name;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public stu() {
    }

    public stu(Long id, String name) {
        System.out.println(Thread.currentThread().getName()+"---------构造");
        Id = id;
        this.name = name;
    }
}
