package com.rxh.payInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface PayThreadPool {

    //创建固定线程池
    ExecutorService pool = Executors.newFixedThreadPool(50);

}
