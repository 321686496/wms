package com.hongan.template.base.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPollUtil {
    private static int corePoolSize =
            Runtime.getRuntime().availableProcessors();
    /**
     * corePoolSize⽤于指定核⼼线程数量
     * maximumPoolSize指定最⼤线程数
     * keepAliveTime和TimeUnit指定线程空闲后的最⼤存活时间
     */
    public static ThreadPoolExecutor executor = new
            ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));
}
