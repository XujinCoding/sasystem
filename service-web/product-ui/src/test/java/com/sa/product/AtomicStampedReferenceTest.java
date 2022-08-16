package com.sa.product;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {
    @Test
    public void koABAQuestion(){
        AtomicStampedReference<Integer> stamp = new AtomicStampedReference(1,1);

        new Thread(() -> {
            System.out.println("线程A 还未改变，版本号为： " + stamp.getStamp());

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean one = stamp.compareAndSet(1, 2, stamp.getStamp(), stamp.getStamp() + 1);
            System.out.println("线程A 第一次改变是否成功: " + one);
            System.out.println("线程A 第一次改变，版本号为： " + stamp.getStamp());

            boolean two = stamp.compareAndSet(2, 1, stamp.getStamp(), stamp.getStamp() + 1);
            System.out.println("线程A 第二次改变是否成功: " + two);
            System.out.println("线程A 第二次改变，版本号为： " + stamp.getStamp());

        },"A").start();

        new Thread(() -> {
            System.out.println("线程B 还未改变，版本号为： " + stamp.getStamp());

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean one = stamp.compareAndSet(1, 3, stamp.getStamp(), stamp.getStamp() + 1);
            System.out.println("线程B 第一次改变是否成功: " + one);
            System.out.println("线程B 第一次改变，版本号为： " + stamp.getStamp());

        },"B").start();

    }
}
