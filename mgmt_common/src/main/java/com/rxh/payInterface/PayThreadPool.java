package com.rxh.payInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface PayThreadPool {
    ExecutorService pool = Executors.newCachedThreadPool();
}
