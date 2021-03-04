package com.yanzhenyidai.wiki.example.guava;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author frank
 * @version 1.0
 * @date 2020-07-24 11:42
 */
public class RateLimiterTest {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1);

        for (int i = 1; i < 10; i = i + 2) {
            double waitTime = rateLimiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }
}
