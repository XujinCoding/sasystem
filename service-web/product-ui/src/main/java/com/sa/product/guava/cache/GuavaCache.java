package com.sa.product.guava.cache;

import com.google.common.cache.*;
import com.sa.product.utils.SpringUtil;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuavaCache {

    private static IProductService  productService= SpringUtil.getBean(IProductService.class);

    private static LoadingCache<Long, ProductDTO> loddingPut = null;

    private GuavaCache(IProductService productService) {
        this.productService = productService;
    }

    /**
     * 获取缓存对象
     * @return  返回一个Cache
     */
    public static LoadingCache<Long, ProductDTO> getCache(){
        //使用双重校验锁机制创建单例模式
        if(Objects.isNull(loddingPut)){
            synchronized (GuavaCache.class){
                if(Objects.isNull(loddingPut)){
                    loddingPut = initCache();
                }
            }
        }
        return loddingPut;
    }

    private static LoadingCache<Long, ProductDTO> initCache(){
        //这里就是创建一个移除监听, 哪个<K,V>被移除了,就调用一下这个方法
        RemovalListener<Long, ProductDTO> removalListener = new RemovalListener<>() {
            @Override
            public void onRemoval(RemovalNotification<Long, ProductDTO> notification) {
                System.out.println(notification.getKey() + ":" + notification.getValue());
            }
        };

        return CacheBuilder.newBuilder()
                //初始容量: 应该设置一个合适的大小, 由于Guava的缓存使用了分段锁机制,所以扩容的代价非常昂贵
                .initialCapacity(100)
                //最大缓存数量: 缓存数目可能在逼近极限值的时候,或者到达极限值的时候进行内存回收, 会将最近没有使用或者总体上很少使用的缓存项进行回收
                .maximumSize(1000)
                //设置缓存的并发级别为CPU核心数,就是同一时刻的并发数, 默认为4, 一般设置为CPU的核心数量
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                //过期策略: 当数据到达指定时间没有被读或者写, 这个数据将会被作为过期数据进行处理, 当没有数据或者读到过期数据的时候只允许一个线程去更新数据,其他线程阻塞,等待更新完成在获取新的值
                .expireAfterAccess(10, TimeUnit.SECONDS)
                //过期策略: 当数据达到指定时间没有被写, 这个数据就会被作为过期数据进行处理, 当没有数据或者读到过期的数据的时候只允许一个线程去更新数据,其他线程阻塞,等待更新完成在获取新的值
//                .expireAfterWrite(30, TimeUnit.SECONDS)
                //过期策略: 数据在指定时间没有被更新, 则为过期数据, 当有线程正在更新数据的时候, 访问的线程返回旧数据
//                .refreshAfterWrite();
                .removalListener(removalListener)
                .build(new CacheLoader<Long, ProductDTO>() {
                    @Override
                    public ProductDTO load( Long key) throws Exception {
                        return productService.findById(key);
                    }
                });
    }
}
