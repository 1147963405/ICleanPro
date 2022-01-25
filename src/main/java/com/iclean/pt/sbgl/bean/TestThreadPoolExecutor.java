package com.iclean.pt.sbgl.bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉;
 * 〈功能详细描述〉
 *
 * @author jxx
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestThreadPoolExecutor {

    public static void main(String[] args) {

        //创建使用固定线程数的线程池
       /* ExecutorService es2 = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            es2.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "正在执行任务");
                }
            });
        }*/
        //创建一个会根据需要创建新线程的线程池
       /* ExecutorService es3 = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            es3.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "正在执行任务");
                }
            });
        }*/
        //创建拥有固定线程数量的定时线程任务的线程池
        ScheduledExecutorService es4 = Executors.newScheduledThreadPool(3);
        System.out.println("时间：" + System.currentTimeMillis());
//        for (int i = 0; i < 3; i++) {
            es4.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("时间："+System.currentTimeMillis()+"--"+Thread.currentThread().getName() + "正在执行任务");
                }
            },3, TimeUnit.SECONDS);
            es4.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("时间："+System.currentTimeMillis()+"--"+Thread.currentThread().getName() + "正在执行任务2");
                }
            },5, TimeUnit.SECONDS);
//        }
        //创建只有一个线程的定时线程任务的线程池
        /*ScheduledExecutorService es5 = Executors.newSingleThreadScheduledExecutor();
        System.out.println("时间：" + System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            es5.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("时间："+System.currentTimeMillis()+"--"+Thread.currentThread().getName() + "正在执行任务");
                }
            },3, TimeUnit.SECONDS);
        }*/
    }
}