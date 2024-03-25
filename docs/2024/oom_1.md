# Java应用内存溢出排查

![oom.jpg](https://s2.loli.net/2024/03/19/zcqIgZBAveaYpuE.jpg)

> 很久没有技术性的写过博客，正巧在长沙这家公司碰到了一个应用经常内存溢出的问题，虽然暂时还没确认解决，也是把以往的一些经验总结一下。

---

## 前言

起初这个项目算是一个祖传项目，而且环境来说相当之复杂，部署在客户的云平台上，实时日志不能查看，输入命令也输入不了，况且项目还是从离职人员手中接过来的，算是很坑的项目。
当时上一个人已经离职，离职之后出现内存溢出，改了定时任务的线程池数量，把数量减少一半后，系统坚挺了一个月，后续则是频繁出现应用宕机的情况。

确实当时我也没什么想法来看这个问题，总是先拿日志，然后看了看，改几个参数丢上去继续跑，毕竟要是想着要跑路的人。后来没事干，想着还是看一看问题，其实解决OOM的问题也还好，
无非就是借助工具，可以用[Arthas](https://arthas.aliyun.com/) ，但是这个环境我们是连接不上不能使用工具，于是想起了以前的项目上经常出现hprof的文件，就开始了下一步。

---

## OOM异常堆栈文件(hprof)

> 此处来自网上的摘抄：

hprof文件是Java虚拟机（JVM）生成的一种二进制文件，用于收集Java应用程序的运行时信息。它包含了Java堆的详细信息，例如对象的数量、类型、大小、引用关系等。
此外，hprof文件还包含了线程的信息、类的信息、方法的信息等。通过分析hprof文件，开发人员可以了解Java应用程序的内存使用情况、对象生命周期、线程状态等信息，从而帮助他们诊断和解决性能问题。

需要生成hprof文件也很简单：只需要在JVM启动参数中增加：

> -XX:+HeapDumpOnOutOfMemoryError：当JVM发生OutOfMemoryError错误时，自动生成hprof文件。

> -XX:HeapDumpPath=<path>：指定hprof文件的输出路径。

> -XX:HeapDumpInterval=<seconds>：指定hprof文件生成的时间间隔。

> -XX:StartFlightRecording：启用Java飞行记录器（JFR），它可以记录应用程序的性能数据，并生成hprof文件。

> -XX:FlightRecorderOptions：指定Java飞行记录器的配置选项。

我一般都是加前两项：`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp`，这个时候如果发生内存溢出，则在tmp下生成一个java_pid***.hprof的文件信息。
 
---

## 分析hprof文件

需要分析hprof文件，这个时候就需要使用工具了，我用的是Eclipse MAT，话说这玩意是真的难找安装包。我找到的是一个可以使用jdk1.8的安装包，上传到了我的百度网盘。
网盘地址[MAT百度云地址，提取码1qaz](https://pan.baidu.com/s/14TS2LK0WTgYdwsnE9hYlCQ) 。

打开工具后，可以在左上方File->Open File，选择hprof文件信息，等待导入完成后，可以看到下图（注意需要点Report下的Leak Suspects，会生成饼图信息）：

![photo_mat.png](https://s2.loli.net/2024/03/20/Ll8XR2BC7Y9EcoS.png)

一般问题都在第一个Problem Suspect1中，点击详情进去查看，找到业务代码信息，然后排查自己的项目中的对应代码。像我这里就是因为删除时，进行查询了单表信息，但是却没有传条件，
所以触发了全表查询，导致的内存溢出。

---

## 结尾

OOM是一个老生常谈的问题，解决方案也是有各种各样，我这里只记录了一下自己所遇到的情况。💻

--- 

## 后续补充

 由于一些项目是用Docker进行部署，且大多数Dockerfile引用的JDK为Openjdk，使用上面的jvm配置项生成的文件为.phd结尾的文件，所以这个就需要使用`IBM Heapanalyzer` 来进行分析排查。

用法也很简单，首先需要下载一个IBM提供的jar包，[IBM Heapanalyzer百度云地址，提取码1qaz](https://pan.baidu.com/s/15cFeQlymp41782MjC8unYg)

下载完成后，需要进行如下配置（保存为.bat文件）：

```bat
title ibm-heap-analyzer

path=%PATH%;%D:\Program Files\Java\jdk1.8.0_311\bin

D:

cd D:\heapanalyzer

java.exe -Xms1048M -Xmx8192M -jar ha457.jar
```

 保存后直接运行.bat文件即可。