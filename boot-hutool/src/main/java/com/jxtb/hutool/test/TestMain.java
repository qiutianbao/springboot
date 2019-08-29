package com.jxtb.hutool.test;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by jxtb on 2019/8/6.
 */
public class TestMain {
    transient String aa ;
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(
                    (t1, e) -> {
                        System.out.println(t1.getName() + "线程抛出的异常"+e);
                    });
            return t;
        });
        threadPool.execute(()->{
            Object object = null;
            System.out.print("result## " + object.toString());
        });
        ArrayBlockingQueue fairQueue = new ArrayBlockingQueue(1000,true);

    }
}
