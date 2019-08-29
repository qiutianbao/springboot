package com.jxtb.hutool.test;

import sun.misc.Unsafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jxtb on 2019/8/6.
 */
public class ThreadPoolTest {

    //newFixedThreadPool
    /*
    使用场景
FixedThreadPool 适用于处理CPU密集型的任务，确保CPU在长期被工作线程使用的情况下，尽可能的少的分配线程，即适用执行长期的任务。
     */
    public static void newFixedThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            });
        }
    }

    //newCachedThreadPool

    /**
     使用场景
     用于并发执行大量短期的小任务。
     */
    public static void newCachedThreadPool(){
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName()+"正在执行");
            });
        }
    }

    //newSingleThreadExecutor

    /**
     使用场景
     适用于串行执行任务的场景，一个任务一个任务地执行
     */
    public static void newSingleThreadExecutor(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName()+"正在执行");
            });
        }
    }

    //newScheduledThreadPool

    /**
     使用场景
     周期性执行任务的场景，需要限制线程数量的场景
     */
    public static void newScheduledThreadPool1(){
        /**
         创建一个给定初始延迟的间隔性的任务，之后的下次执行时间是上一次任务从执行到结束所需要的时间+* 给定的间隔时间
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            System.out.println("current Time" + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+"正在执行");
        }, 1, 3, TimeUnit.SECONDS);
    }
    public static void newScheduledThreadPool2(){
        /**
         创建一个给定初始延迟的间隔性的任务，之后的每次任务执行时间为 初始延迟 + N * delay(间隔)
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println("current Time" + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+"正在执行");
        }, 1, 3, TimeUnit.SECONDS);
    }
    private static final Unsafe unsafe = Unsafe.getUnsafe();

}
