
# 一、为什么有这个想法

> 大概在2018年末，2019年初，项目中使用DUBBO框架很长一段时间后，开始了解其他的微服务框架，比如SpringBoot，SpringCloud，grpc，brpc，这些都是很优秀的开源项目。<br/>
  而我也是从这段时间了解过程中，感觉SpringCloud使用起来很方便，加上Spring的原因，更加的容易上手，所以想自己使用SpringCloud搭建一套微服务框架，算是在自己的程序员职业生涯留下一些可以纪念的东西。
 
---
## 架构选型

 关于架构选型，由于自己项目使用Dubbo比较多，对Dubbo也比较熟悉，所以直接就放弃掉了Dubbo，Dubbo给我的感觉就像是：食之无味，但是还是要吃它。功能很强大，感觉有点冗余。对Dubbo的体验，可以查看 [dubbo]() 这篇博客。
 
 而SpringBoot，给我的就有一种大道至简的感觉，不需要配置太多太多的xml，简单的几个依赖，几个注解，一个微服务就能立马运行，“一瓶保你用三年,再送好礼真心赞”。对SpringBoot的体验，可以查看 [springboot]() 这篇博客。
 
 SpringCloud作为以Springboot为基础，在其之上增加了不少的开源组件，在微服务能运行起来后，服务监控、断路器、链路跟踪，简直不要太强大的功能。SpringCloud可以查看 [springcloud]() 这篇博客。
 
> 一张参加Dubbo线下活动的照片

 <img src="https://i.loli.net/2020/03/25/iAsjmW5t4HQc1rV.jpg" width="80%"/>
 
 可以看到阿里的同学为了推广自己的Dubbo也是花了不少功夫，在现场晒出这张图时，底下真的是嘘声一片。 :joy:
 

---
