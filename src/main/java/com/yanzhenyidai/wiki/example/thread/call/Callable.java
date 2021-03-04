package com.yanzhenyidai.wiki.example.thread.call;

/**
 * @author frank
 * @version 1.0
 * @date 2020-06-30 16:13
 */
public class Callable implements java.util.concurrent.Callable<Object> {

    public Integer num;

    public Callable(int num) {
        this.num = num;
    }

    @Override
    public Object call() throws Exception {

        System.out.println("这是第" + num + "个线程");

        return null;
    }
}
