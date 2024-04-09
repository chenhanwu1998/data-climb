package com.chw.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SingleThreadPool {

    private static Integer coreSize = 10;
    private static Integer maxSize = 32;
    private static Integer keepAliveTime = 30;
    private static Integer queueSize = 200;

    private static ThreadPoolExecutor poolExecutor;


    static {
        poolExecutor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), new ThreadPoolExecutor.AbortPolicy());
    }


    public static synchronized void execute(Runnable runnable) {
        while (poolExecutor.getQueue().size() >= queueSize - 1) {
            try {
                Thread.sleep(1000);
                log.info("线程等待队列过多，休眠等待，防止被拒绝");
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        poolExecutor.execute(runnable);
    }

    /**
     * 卡住主线程，等待当前的所有任务执行完成
     */
    public static void waitForEnd() {
        long workNum = poolExecutor.getTaskCount();
        while (poolExecutor.getCompletedTaskCount() < workNum) {
            log.info("等待任务执行结束");
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    public static void main(String[] args) {
        AtomicInteger index = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("运行中1   " + index.incrementAndGet());
            };
            SingleThreadPool.execute(runnable);
        }
        log.info("运行结束1");
        waitForEnd();
        log.info("taskCount:" + poolExecutor.getTaskCount());
        AtomicInteger index2 = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("运行中2   " + index2.incrementAndGet());
            };
            SingleThreadPool.execute(runnable);
        }
        log.info("taskCount:" + poolExecutor.getTaskCount());
        waitForEnd();
        log.info("运行结束2");
    }

}
