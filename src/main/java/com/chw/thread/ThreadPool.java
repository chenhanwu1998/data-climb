package com.chw.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPool {

    private static Integer coreSize = 5;
    private static Integer maxSize = 10;
    private static Integer keepAliveTime = 30;
    private static Integer queueSize = 20;

    private ThreadPoolExecutor poolExecutor;

    public ThreadPool() {
        poolExecutor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), new ThreadPoolExecutor.AbortPolicy());
    }


    public void execute(Runnable runnable) {
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

    public void waitForEnd() {
        poolExecutor.shutdown();
        // 等待线程运行结束
        while (!poolExecutor.isTerminated()) {
        }
        log.info("线程池任务运行完成，关闭线程池");
        poolExecutor = null;
    }


    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("运行中");
            };
            threadPool.execute(runnable);
        }
        threadPool.waitForEnd();
        log.info("运行结束1");

        ThreadPool threadPool2 = new ThreadPool();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("运行中");
            };
            threadPool2.execute(runnable);
        }
        threadPool2.waitForEnd();
        log.info("运行结束2");
    }

}
