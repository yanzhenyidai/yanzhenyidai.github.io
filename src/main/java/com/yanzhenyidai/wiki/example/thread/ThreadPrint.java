package com.yanzhenyidai.wiki.example.thread;

/**
 * 打印奇偶数
 *
 * @author frank
 * @version 1.0
 * @date 2020-03-21 15:30
 */
public class ThreadPrint {

    public static volatile int num = 0;

    public static void main(String[] args) {
        while (num < 100) {
            num++;

            Thread thread1 = new Thread(new Runnable() {

                public void run() {

                    if (num % 2 == 0) {
                        System.out.println("偶数=======" + num);
                    }
                }
            });


            Thread thread2 = new Thread(new Runnable() {

                public void run() {
                    if (num % 2 != 0) {
                        System.out.println("=================奇数" + num);
                    }
                }
            });

            thread1.run();
            thread2.run();
        }

    }


}
