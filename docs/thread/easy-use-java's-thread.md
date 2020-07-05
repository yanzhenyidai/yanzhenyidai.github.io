
# 使用线程进行多批量请求
 
 ![thread-icon.jpg](https://i.loli.net/2020/07/05/xG9bIeSB8jpz5yR.jpg)
 
 > 多线程，无论是在工作，或者面试过程中，都是作为一个有高光亮点的名词，其实作为一个工具，它有的时候又不像大家说的那么复杂。

---

## 介绍

 多线程其实在几年前的时候就开始了解，不过都是作为一些 Demo 出现，这会还是在项目中首次运用到。
 
 > 发生的业务如下：

 在项目中有对图片进行处理的操作，开始是上传一张图片的同时，只对这一张图片进行操作，而用户使用了下来，提出一张图片一张图片的上传效率太慢，而且等待时间过久，
 于是，大家都懂的，需求开始改动了，改成多张图片上传，一次处理。
 
 评估了一下可行性，于是考虑到了使用线程来进行处理。
 
 > 多线程的基础知识（Java）
 
 - 关键字
    - volatile
    - synchronized
 
 - 实现方式
    - Thread类
    - Runnable接口
    - Callable接口
    
 - 线程池框架
    - Executor框架

---

## 使用

 使用起来没什么多大的难度，因为如果只要你是使用JDK的线程并发包和工具类，基本上都有实际案例，而且都已经有了优化。
 
 > ExecutorService
 
 这里实现的是Callable的接口，因为图片处理完之后，需要拿到图片结果信息，而Runnable接口是拿不到返回值信息的。
 
```java

public static void main(String[] args) throws InterruptedException, ExecutionException {
    long time1 = System.currentTimeMillis();

    ExecutorService service = Executors.newFixedThreadPool(5);

    for (int i = 0; i < 10; i++) {
        int finalI = i;

        java.util.concurrent.Callable<Object> callable1 = new java.util.concurrent.Callable() {
            @Override
            public Object call() throws Exception {
               
                // 实际业务逻辑
                
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
```
 
### **补充，Executor的成员信息**

 > 使用了Executor框架，就需要了解ThreadPoolExecutor的三个主要实现方法：
 
 - newFixedThreadPool
 创建固定的线程，多个，一直等待前一个线程执行才执行后一个线程。
 
 - newSingleThreadExecutor
 创建单个的线程。
 
 - newCachedThreadPool
 无限制线程数量。
 
 > ScheduledThreadPoolExecutor
 
 ScheduledThreadPoolExecutor 主要用于线程内定时执行的逻辑出现，这个目前自己用到比较少，可以看下[Java中调度线程池ScheduledThreadPoolExecutor原理探究](https://ifeve.com/33981-2/)
 
 > Future 
 
 主要是用来表示线程异步计算后的结果，以 `Future<V>` 的形式出现。
 
 > Runnable 和 Callable
 
 上面说过，Runnable不会返回执行结果，而Callable可以返回执行结果。
 
 对于需要使用多线程的业务来说，不仅仅只要求代码上来实现，更多的是取决于服务器性能，比如一个单核的服务器，要开八个线程来处理业务逻辑，这样就只能无话可说。

---

## 总结

 多线程能用好就说明程序员功力已经达到了某种炉火纯青的地步。
 
 参考资料：
 
 > Java并发编程的艺术 书籍
 > [并发编程网](https://ifeve.com)