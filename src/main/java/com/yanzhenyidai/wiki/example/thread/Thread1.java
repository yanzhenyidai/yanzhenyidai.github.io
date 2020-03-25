package com.yanzhenyidai.wiki.example.thread;

/**
 * @author frank
 * @version 1.0
 * @date 2020-03-25 10:49
 */
public class Thread1 implements Runnable {

    public void run() {

        while (ThreadPrint.num < 100) {
            ThreadPrint.num++;
            if (ThreadPrint.num % 2 != 0) {
                System.out.println("调用线程1打印" + ThreadPrint.num);
//                continue;
            }
//            System.out.println("调用线程1打印" + ThreadPrint.num);
        }

    }
}
