package com.yanzhenyidai.wiki.example.thread;

public class Thread2 implements Runnable {

    public void run() {
        while (ThreadPrint.num < 100) {
            ThreadPrint.num++;
            if (ThreadPrint.num % 2 == 0) {
                System.out.println("调用线程2打印" + ThreadPrint.num);
//                continue;
            }
//            System.out.println("调用线程1打印" + ThreadPrint.num);
        }

    }
}