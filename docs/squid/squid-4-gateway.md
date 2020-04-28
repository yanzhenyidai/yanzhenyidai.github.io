
# 搭建微服务框架（服务网关处理）

> 本篇来进行微服务网关集成的使用操作

---

## Spring-Cloud-Gateway

 由于SpringCloud-Netflix的Zuul组件不再维护，而Spring官方推出了Gateway的新组件，并且支持了SringCloud2.0的版本，所以在选型方面，直接就选择了Spring官方的Gateway。

 介绍Gateway，不得不将它与Zuul进行比较。
 
 的确，Zuul的网关处理流程很一目了然，基于一个 `ZuulFilter`，而后可以定义 `preRoute()` `route()` `postRoute()` `error()`，类似于Spring的前置通知，后置通知，环绕通知，算得上是不错的网关处理组件，
 比较可惜的是，SpringCloud-Netfilx的停止更新，使得SpringCloud的网关处理选择为了 `Spring-Cloud-Gateway`。
 
 众所周知，Zuul是基于Serverlet，而Gateway是基于Netty，两个谁更优秀，这个目前也是进行不了一个定义，广义上来说也是传统的 `Http和TCP` 的比较。
 
 下面介绍Gateway的使用，👇
 
---

## 使用Spring-Cloud-Gateway
 
 首先贴一下工程截图：
 
 ![squid-gateway.png](https://i.loli.net/2020/04/28/JYPyxuQztIbRgdB.png)
 
 很简单的一个例子，先新建一个 `squid-gateway` 的工程，引入如下依赖：
 
 > pom.xml
 
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-nacos-discovery</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>io.projectreactor</groupId>
        <artifactId>reactor-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-gateway-core</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
    <dependency>
        <groupId>org.isomorphism</groupId>
        <artifactId>token-bucket</artifactId>
        <version>1.7</version>
    </dependency>
```

 > GatewayApplication

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }
}
```

 > application.yaml
 
 最主要的就是application.yaml文件了，里面可以配置路由转发规则，当然也可以直接在 `GatewayApplication.java` 文件中写java代码来进行转发，不过我总认为这样不太直观，下面来看一下文件：
 
```yaml
server:
  port: 8006

spring:
  application:
    name: squid-gateway
  cloud:
    nacos:
      discovery:
        server-addr: yanzhenyidai.com:8848
    gateway:
#      default-filters:
#      - PrefixPath=/gateway
#      - AddResponseHeader=X-Response-Default-Foo, Default-Bar
      routes:
      - id: squid-example-dubbo-provider
        uri: lb://squid-example-dubbo-provider
        predicates:
        - Path=/dubbo/**
        filters:
        - StripPrefix=1

      - id: squid-oauth2
        uri: lb://squid-oauth2
        predicates:
        - Path=/oauth/**
```

 关键的两点：
 
 - Nacos
 
 Nacos不用多说，之前介绍的本次使用的注册服务中心，配置好Nacos的信息，在routes下转发会直接发现到注册好的实例ID服务，进行请求访问并返回处理结果信息。
 
 - Gateway-routes

 routes中配置的信息大致如下：
 
 1. id 为唯一值，可以使用项目模块名配置。
 2. uri 为路径，lb代表 `loadblance` 的意思，后续跟随的可以为 IP 请求地址，也可以为注册中心中的示例ID名称。
 3. predicates 代表正则匹配规则。
 4. Path 为请求时的简写路径。
 5. StripPrefix 请求过滤的地方，1则表示过滤简称的第一项。
 
---

## 总结

 当然Gateway远远不止以上这么多的配置信息，因为目前对Gateway还是处于了解中，并没有深入的读它的设计理念。
 
 对于Gateway，个人觉得，如果服务器上已经有了Nginx，并且工程中外部的请求没有很多需要特别逻辑处理，直接用Nginx做网关就可以了，而这个也正是我没有深入去了解Gateway的原因。😜
 
 参考资料：
 
 > [Spring-Cloud-Gateway 2.2.2.RELEASE(WIKI)](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.2.RELEASE/reference/html/) <br>
 > [Spring-Cloud-Gateway (GITHUB)](https://github.com/spring-cloud/spring-cloud-gateway) <br>
 > [Spring-Cloud-Gateway (Spring.io)](https://spring.io/projects/spring-cloud-gateway#overview)
 > [BAELDUNG.com (GateWay)](https://www.baeldung.com/spring-cloud-gateway)

