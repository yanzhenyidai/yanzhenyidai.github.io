
# Dubbo

 ![dubbo.jpg](https://i.loli.net/2020/04/05/jvbg4yfUTEst8SJ.png)

> 假期第二天，把Dubbo的一些理解整理了一下，对于大家广义上的Dubbo是一款分布式，高可用，负载均衡的远程调用框架，而对于做过的项目中狭义上来说，以上功能没用到过一个，属于完完全全的单机部署，所以以下只能算是个人理解，毕竟没有在生产上实际运用到分布式。

---

## 介绍Dubbo

 Dubbo的介绍网上有很多，这里不做太多介绍，只需要知道它是一款分布式的RPC框架，由阿里巴巴开发并已经开源，[Dubbo官方地址](http://dubbo.apache.org/en-us/)，[Github地址](https://github.com/apache/dubbo)。
 
 而Dubbo的RPC流程，这里贴一张官方的调用流程图。
 
 ![architecture.png](https://i.loli.net/2020/04/05/jmN2ktaL8fPOzdx.png)
  
 上图基本可以看出Dubbo的调用流程，不过第5点 `count Monitor` 这一块，应该是在最近的Dubbo2.7.x版本增加的，之前的2.5.x版本没有监控的这块逻辑。 

---

## 使用Dubbo

 Dubbo使用起来也比较方便（当然是没有Springboot方便啦:wink:），版本可以使用Spring，SpringBoot，SpringCloud-Alibaba。
 
 Dubbo官方是推荐的Spring，这种方式在个人看来是配置xml文件太多，当然方便的地方也有很多很多，举例来说直接改xml文件比起改代码靠谱很多。
 
 而[SpringBoot Dubbo](https://github.com/apache/dubbo-spring-boot-project)，使用起来没有太多的注解，代码整洁度很高，配合Springboot启动Dubbo服务，也是如虎添翼，完全可以用一句“简直不用太爽”来形容。
 
 SpringCloud-Alibaba，作为阿里强推的SpringCloud-Nextflix的替代版本，只能说：嗯，不错！
 
 > 下面以Spring版本来说明
 
### Register
 
 Dubbo的调用流程如之前的图上来说，首先需要准备注册中心，官方推荐的是Zookeeper，个人认为Zookeeper很不错，不过也可以了解以下Nacos，当然没有注册中心可以先进行本地注册和调用，只需要将注册中心地址改成 `N/A` 就可以。
 
 这里说明一下Zookeeper，Zookeeper是以节点来进行服务注册和消费，很像二叉树算法，当然很多框架和数据库都是运用了二叉树。
 
 而我们使用Zookeeper时，也可以使用它的节点的特征，来进行锁的实现，将特定的唯一的key注册到节点，根据节点是否存在来判断是否锁定，分布式锁的问题也不会有太大的问题，只需要将Zookeeper配置集群，相互监听Zookeeper的心跳。
 
### Provider

 Provider基本上是以接口的形式进行注册到Zookeeper，如果看过Zookeeper下的节点的同学都会发现，注册路径 `dubbo://com.yanzhenyidai.squid.example.SquidService` 这样的形式，首先 `dubbo` 是协议名，其他的则是java
 项目中的包路径。
 
### Consumer

 Consumer和Provider设计类似，调用也是通过Zookeeper注册的节点来进行。
 
### 其他功能

#### 泛化调用
 
 泛化调用的存在意义在于，比如你是使用的Java语言，而对方调用者使用的GO，GO的这一方不可能按照Java的规范来编写接口进行调用，Dubbo的设计还是很有前瞻性。
 
 只需要在 `<dubbo:reference>` 中的 `generic` 设置为true，接口就被声明成了泛化接口，而后使用 `GenericService` 进行invoke，类似代码如下：
 
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
      
    <dubbo:application name="demotest-consumer" owner="programmer" organization="dubbox"/>
    <!--向 zookeeper 订阅 provider 的地址，由 zookeeper 定时推送-->
    <dubbo:registry address="zookeeper://localhost:2181"/>
    <!--使用 dubbo 协议调用定义好的 api.PermissionService 接口-->
    <dubbo:reference id="permissionService" interface="com.alibaba.dubbo.demo.DemoService" generic="true"/>
</beans>
```
 
 ```java
package com.alibaba.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.service.GenericService;

public class Consumer {
    public static void main(String[] args) {
        /////////////////Spring泛化调用/////////  
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        System.out.println("consumer start");
        GenericService demoService = (GenericService) context.getBean("permissionService");
        System.out.println("consumer");
        Object result = demoService.$invoke("getPermissions", new String[] { "java.lang.Long" }, new Object[]{ 1L });
        System.out.println(result);
    }
}
```

 补充说明的是，虽然泛化看起来是很麻烦，没有Rest接口来的快，但是设计出来就是有用处的，真的是必不可缺的。

#### 负载均衡

 Dubbo的负载均衡没有像SpringCloud Robbin那样，有专门的 `LoadBanlance`，看过Dubbo-Admin的同学应该都知道里面的服务都有一个叫做权重的概念，而Dubbo的负载均衡正是以权重来进行的计算。
 
 ```java
public class RandomLoadBalance extends AbstractLoadBalance {
    public static final String NAME = "random";

    public RandomLoadBalance() {
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size();
        boolean sameWeight = true;
        int[] weights = new int[length];
        int firstWeight = this.getWeight((Invoker)invokers.get(0), invocation);
        weights[0] = firstWeight;
        int totalWeight = firstWeight;

        int offset;
        int i;
        for(offset = 1; offset < length; ++offset) {
            i = this.getWeight((Invoker)invokers.get(offset), invocation);
            weights[offset] = i;
            totalWeight += i;
            if(sameWeight && i != firstWeight) {
                sameWeight = false;
            }
        }

        if(totalWeight > 0 && !sameWeight) {
            offset = ThreadLocalRandom.current().nextInt(totalWeight);

            for(i = 0; i < length; ++i) {
                offset -= weights[i];
                if(offset < 0) {
                    return (Invoker)invokers.get(i);
                }
            }
        }

        return (Invoker)invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
}
```

 可以看到，Dubbo的算法是先获取到所有服务的权重总数，然后按照每个服务的权重进行百分比的分配，而如果权重都是一样的，则直接进行随机调用。
 
#### SPI

 SPI作为java中设计的牛逼的拓展，你完全可以在 `META-INF/services` 下创建文件，一般是文件名为service名称，文件里加载指定的实现类，类似于笔记本可以外接键盘的场景。
 
 而Dubbo的SPI，官方说明重新定义了一套规则，在新dubbo的版本中，`ExtensionLoader` 类中，`loadResource`方法，源码中确实是使用IOC的逻辑，需要注意的是，与java SPI不同，dubbo的SPI文件里的
 内容是以key-value出现。
 
#### PojoUtil

 最后写一下dubbo的序列化机制，之前在2.5.x版本中被吐槽惨了，大部分请求传输量比较大的公司都进行了序列化替换。而在PojoUtil中，dubbo实现的是进行Map和Object互转。如果是传输byte字节，只有在 `<dubbo:protocol>` 中将ploy容量增大。

---

## 部署Dubbo

 部署Dubbo的方式，按照服务来划分的话，部署起来很方便，单独的将服务启动，前端功能最后启动。
 
 如果是做分布式的话，因为之前没有过类似经验，但是自己慢慢研究，得出如下方式：
 
 将Zookeeper进行相互监听，最好是三台或者三台以上的奇数形式存在，大家都知道Zookeeper的选举机制是按照单数的形式来进行运算。Zookeeper相互监听，而Dubbo服务注册的url地址则要分别加上所有的Zookeeper的地址，以`;`
 结尾，例如 `192.168.10.1:2181;192.168.10.2:2181`，这样注册成功后，在Dubbo-Admin上可以看到，服务分别注册到每个Zookeeper上。
 
 而上面说到的负载均衡机制，这个地方可以判断服务器配置来进行配置权重，可以使用1台高配置服务器，两台低配置服务器，大部分服务可以在高配置服务器上调用，一部分放到低配的服务器上。
 
 分布式部署需要注意的是，日志文件一定要按照每天，每个文件的最大size进行划分，比如每个文件最大只能存储20MB，这样查找日志起来也很方便。

---

## Dubbo中遇到的坑

 Dubbo中遇到的坑太多，一时间回想不起来，大体存在如下：
 
 - 服务重复调用，解决方案，重试机制大部分放到provider配置，consumer一定配置要为0
 - IPv4和IPv6，使用java参数配置，仅支持IPv4启动，毕竟现在IPv6还没有普及。
 
---

## 总结

 总结下来，Dubbo真的是用到过的最多的分布式框架，写出来的内容都是最多的，不过由于Dubbo用的太多，而SpringCloud难度比Dubbo要小很多，现在大部分项目都转投到了SpringCloud上面，后面也会出更多的SpringCloud相关的
 使用心得文章，Dubbo的文档大家真的可以多看官方文档，非常成熟的文档结构可以帮助使用Dubbo的同学更快的定位到服务问题，最主要的原因中文文档很多。


