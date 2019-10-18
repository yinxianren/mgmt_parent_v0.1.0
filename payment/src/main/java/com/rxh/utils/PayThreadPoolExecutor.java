package com.rxh.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PayThreadPoolExecutor {


   private static ThreadPoolExecutor executor=new ThreadPoolExecutor(25,50,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());



   public static  ThreadPoolExecutor getThreadPoolExecutor(){
       return executor;
   }
}
