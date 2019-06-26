package com.jxtb.sdk.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jxtb on 2019/6/14.
 */
public class DefaultThreadFactory implements ThreadFactory{

    private static final AtomicInteger poolId = new AtomicInteger();
    private final AtomicInteger nextId = new AtomicInteger();
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    protected  final ThreadGroup threadGroup;

    public DefaultThreadFactory(String poolName){
        this(poolName, true, Thread.NORM_PRIORITY);
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority){
        this(poolName, daemon, priority, System.getSecurityManager() == null ?
               Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup());
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup){
        if(poolName == null){
            throw new NullPointerException("poolName");
        }
        if(priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY){
            throw new IllegalArgumentException("priority: " + priority + "(expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
        prefix = poolName + "_" + poolId.incrementAndGet() + "_";
        this.daemon = daemon;
        this.priority = priority;
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = newThread(new DefaultRunnableDecorator(r), prefix + nextId.incrementAndGet());
        try{
            if(t.isDaemon()){
                if(!daemon){
                    t.setDaemon(false);
                }
            }else{
                if(daemon){
                    t.setDaemon(true);
                }
            }
            if(t.getPriority() != priority){
                t.setPriority(priority);
            }
        }catch (Exception e){
            ;
        }
        return t;
    }

    protected Thread newThread(Runnable r, String name){
        return new Thread(threadGroup ,r, name);
    }

    private static final class DefaultRunnableDecorator implements Runnable {
        private final Runnable r;
        DefaultRunnableDecorator(Runnable r){this.r = r;}

        @Override
        public void run() {
            r.run();
        }
    }

}
