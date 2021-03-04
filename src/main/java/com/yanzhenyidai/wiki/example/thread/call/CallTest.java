package com.yanzhenyidai.wiki.example.thread.call;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author frank
 * @version 1.0
 * @date 2020-06-30 16:16
 */
public class CallTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long time1 = System.currentTimeMillis();

//        for (int i = 0; i < 10; i++) {
//
//            Thread.sleep(500);
//
//            System.out.println("这是第" + i);
//        }

        ExecutorService service = Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 10; i++) {
            int finalI = i;

            java.util.concurrent.Callable<Object> callable1 = new java.util.concurrent.Callable() {
                @Override
                public Object call() throws Exception {

                    Thread.sleep(500);

                    System.out.println("这是第" + finalI);

                    return finalI;
                }
            };

            Future<Object> future = service.submit(callable1);

            // 返回值
            Object obj = future.get();
        }

        service.shutdown();

        System.out.println("执行时间：" + (System.currentTimeMillis() - time1) + ":ms");
    }

}
