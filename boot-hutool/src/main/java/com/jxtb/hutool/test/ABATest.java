package com.jxtb.hutool.test;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by jxtb on 2019/8/6.
 * ABA问题
 * 解决atomic类cas操作aba问题，解决方式是在更新时设置版本号的方式来解决，每次更新就要设置一个不一样的版本号，修改的时候，不但要比较值有没有变，还要比较版本号对不对，这个思想在zookeeper中也有体现；
 */
public class ABATest {

    public final static AtomicStampedReference<String> ATOMIC_REFERENCE = new AtomicStampedReference<String>("abc", 0);

    /**
     *
     它里面只有一个成员变量,要做原子更新的对象会被封装为Pair对象，并赋值给pair；
     private volatile Pair<V> pair;

     先看它的一个内部类Pair ，要进行原子操作的对象会被封装为Pair对象
     private static class Pair<T> {
     final T reference;     //要进行原子操作的对象
     final int stamp;       //当前的版本号
     private Pair(T reference, int stamp) {
     this.reference = reference;
     this.stamp = stamp;
     }
     static <T> Pair<T> of(T reference, int stamp) { //该静态方法会在AtomicStampedReference的构造方法中被调用，返回一个Pair对象；
     return new Pair<T>(reference, stamp);
     }
     }
     现在再看构造方法就明白了，就是将原子操作的对象封装为pair对象
     public AtomicStampedReference(V initialRef, int initialStamp) {
     pair = Pair.of(initialRef, initialStamp);
     }

     获取版本号
     就是返回成员变量pair的stamp的值
     public int getStamp() {
     return pair.stamp;
     }

     原子修改操作，四个参数分别是旧的对象，将要修改的新的对象，原始的版本号，新的版本号
     这个操作如果成功就会将expectedReference修改为newReference，将版本号expectedStamp修改为newStamp；
     public boolean compareAndSet(V   expectedReference,
     V   newReference,
     int expectedStamp,
     int newStamp) {
     Pair<V> current = pair;

     return
     expectedReference == current.reference && expectedStamp == current.stamp //如果原子操作的对象没有更改，并且版本号也没有更改，
     &&
     (
     (newReference == current.reference &&newStamp == current.stamp) //如果要修改的对象与就的对象相同，并且新的版本号也与旧的版本号相同，也就是重复操作，这时候什么也不干
     ||
     casPair(current, Pair.of(newReference, newStamp)) //cas操作，生成一个新的Pair对象，替换掉旧的，修改成功返回true，修改失败返回false；
     );
     }
     * @param args
     */
    public static void main1(String[] args) {
        for (int i = 0; i < 100; i++) {
            final int num = i;
            final int stamp = ATOMIC_REFERENCE.getStamp();
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ATOMIC_REFERENCE.compareAndSet("abc", "abc2", stamp, stamp + 1)) {
                        System.out.println("我是线程：" + num + ",我获得了锁进行了对象修改！");
                    }
                }
            }.start();
        }
        new Thread() {
            public void run() {
                int stamp = ATOMIC_REFERENCE.getStamp();
                while (!ATOMIC_REFERENCE.compareAndSet("abc2", "abc", stamp, stamp + 1))
                    ;
                System.out.println("已经改回为原始值！");
            }
        }.start();
    }


    /**AtomicMarkableReference 解决aba问题，注意，它并不能解决aba的问题 ，它是通过一个boolean来标记是否更改，本质就是只有true和false两种版本来回切换，只能降低aba问题发生的几率，并不能阻止aba问题的发生，看下面的例子**/
    public final static AtomicMarkableReference<String> ATOMIC_MARKABLE_REFERENCE = new AtomicMarkableReference<String>("abc" , false);

    public static void main(String[] args){
        //假设以下操作由不同线程执行
        System.out.println("mark:"+ATOMIC_MARKABLE_REFERENCE.isMarked()); //线程1 获取到mark状态为false，原始值为“abc”
        boolean thread1 = ATOMIC_MARKABLE_REFERENCE.isMarked();
        System.out.println("mark:"+ATOMIC_MARKABLE_REFERENCE.isMarked()); //线程3获取到mark状态为false ，原始值为“abc”
        boolean thread2 = ATOMIC_MARKABLE_REFERENCE.isMarked();
        System.out.println("change result:"+ATOMIC_MARKABLE_REFERENCE.compareAndSet("abc", "abc2", thread1, !thread1)); //线程1 更改abc为abc2

        System.out.println("mark:"+ATOMIC_MARKABLE_REFERENCE.isMarked()); //线程2获取到mark状态为true ，原始值为“abc2”
        boolean thread3 = ATOMIC_MARKABLE_REFERENCE.isMarked();
        System.out.println("change result:"+ATOMIC_MARKABLE_REFERENCE.compareAndSet("abc2", "abc", thread3, !thread3));//线程2 更改abc2为abc

        System.out.println("change result:"+ATOMIC_MARKABLE_REFERENCE.compareAndSet("abc", "abc2", thread2, !thread2));//线程3更改abc为abc2；
        //按照上面的执行顺序，3此修改都修改成功了，线程1与线程2拿到的mark状态都是false，他俩要做的操作都是将“abc”更改为“abc2”， 只是线程2在线程1和线程3中间做了一次更改，最后线程2做操作的时候并没有感知到线程1与线程3的更改；
    }


}
