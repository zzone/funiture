package com.app.mvc.test;import com.google.common.collect.Maps;import lombok.extern.slf4j.Slf4j;import java.util.concurrent.ArrayBlockingQueue;import java.util.concurrent.ConcurrentMap;import java.util.concurrent.RejectedExecutionHandler;import java.util.concurrent.ThreadPoolExecutor;import java.util.concurrent.TimeUnit;/** * Created by jimin on 16/3/21. */@Slf4jpublic class TestThreadPool {    private static ConcurrentMap<Integer, ThreadPoolExecutor> threadPoolExecutorMap = Maps.newConcurrentMap();    public static void main(String[] args) throws Exception {        for (int i = 0; i < 3000; i++) {            threadPoolExecutorMap.put(i, newDefaultExecutor());        }        log.info("start!");        for (int j = 0; j < 300; j++) { // 取线程池            log.info("init thread, {}", j + 1);            for(int p = 0 ; p < 100; p ++) { // 100个线程                threadPoolExecutorMap.get(j).submit(new Runnable() {                    @Override                    public void run() {                        double sum = 0;                        for (int q = 0; q < 10000000; q++) {                            sum += q;                        }                        log.info("sum: {}", sum);                    }                });            }        }    }    private static ThreadPoolExecutor newDefaultExecutor() {        log.info("init default");        return new ThreadPoolExecutor(2,          // 核心池大小                5,                                 // 最大线程数                1200,                               // 空闲等待时间                TimeUnit.SECONDS,                     // 时间单位                new ArrayBlockingQueue<Runnable>(50), // 循环数组 + 指定大小                new DiscardOldestPolicy()  // 最早的丢弃(因为调用方可能已经超时了)        );    }    public static class DiscardOldestPolicy implements RejectedExecutionHandler {        public DiscardOldestPolicy() {        }        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {            if (!e.isShutdown()) {                log.info("reject");                e.getQueue().poll();                e.execute(r);            }        }    }}