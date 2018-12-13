package io.taucoin.foundation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static ExecutorService mThreadPool;

    public static ExecutorService getThreadPool() {
        synchronized (ExecutorService.class){
            if(mThreadPool == null){
                mThreadPool = Executors.newCachedThreadPool();
            }
        }
        return mThreadPool;
    }
}
