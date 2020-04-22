
# 搭建微服务框架（SC服务注册与发现）

---

## 服务注册中心-Nacos

 Nacos是阿里开源的一款注册中心中间件，详细介绍可以访问[Nacos](https://nacos.io/zh-cn/index.html)官网，本次框架是基于Nacos做的注册。
 
 安装Nacos我们可以使用Docker，Docker可以很快的安装好Nacos并且启动。
 
 而在本项目中，引入的Nacos两个依赖分别如下：
 
 ```xml
 <dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
 </dependency>

 ```
 
 完成对Nacos的依赖引入后，可以进行下一步的SpringCloud服务发布的操作，👇。
    
---

## SpringCloud服务发布

 我们新建俩个Maven模块项目，用它来实现服务提供者的角色。
 
 ![squid-sc-provider.png](https://i.loli.net/2020/04/19/XnYaFleZbD3xtWE.png)
 
 如上，分别以 `squid-example-api` 和 `squid-example-provider` 命名。
 
 - squid-example-api
 
 `squid-example-api` 项目中主要是写一些接口文件，将这些接口对外注册并且暴露出去，引入依赖：
 
 ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```

 新建一个 `HiService` 的接口，并且注明 `@FeignClient("squid-example-sc-provider")`，注解内名称可以为项目名：
 
 ```java
    @FeignClient("squid-example-sc-provider")
    public interface HiService {
    
        @RequestMapping(value = "/hi", method = RequestMethod.GET)
        public String hi(@RequestParam("msg") String msg);
    }
    
 ```
 

 **PS**：这里是为了明确各个模块，所以将 `squid-example-api` 和 `squid-example-provider` 进行分离，也可以将两个模块合并。
 
 
 - squid-example-provider
 
 然后我们依旧是在 `squid-example-provider` 引入依赖：
 
 ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.yanzhenyidai</groupId>
        <artifactId>squid-example-sc-api</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

 引入 `spring-cloud-start-openfeign` 是将服务以 `Http Restful` 的形式进行对外发布（SC的服务基本上都是以Http Restful的形式，
 可以了解一下 Dubbo或者其他RPC的服务注册，也可以看一下 [RPC和HTTP]()），而 `spring-boot-starter-web` 则是表明该项目需要以Web的形式进行启动，
 不难理解，毕竟HTTP请求是有需要一个Web容器来进行。
 
 一切准备就绪后，我们就可以开始来编写服务实现和启动服务类了。
 
 在项目中最外层的包路径中新建一个 `Application` 的启动类，这点原因熟悉Spring加载逻辑的同学应该都明白，Spring启动扫描时是向下扫描，也就是加载包路径以下的
 代码配置：
 
 ```java
    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableFeignClients
    @RestController
    public class ScProviderApplication {
    
        @Autowired
        private HiService hiService;
    
        public static void main(String[] args) {
            SpringApplication.run(ScProviderApplication.class, args);
        }
    
    
        @GetMapping(value = "/hi")
        public String hi(String msg) {
            return hiService.hi(msg);
        }
    }
    
  ```
  
  继续编写一个 `HiService` 的实现 `HiServiceImpl`：
  
  ```java
    @Service
    public class HiServiceImpl implements HiService {
    
        @Override
        public String hi(String msg) {
            return "hi sc," + msg;
        }
    }
  ```
  
  贴一下 `application.yaml` 文件信息：
  
  ```yaml
    spring:
      application:
        name: squid-example-sc-provider
      cloud:
        nacos:
          discovery:
            server-addr: yanzhenyidai.com:8848
        discovery:
          client:
            health-indicator:
              enabled: false
    server:
      port: 8084
    ribbon:
      ReadTimeout: 100000
      ConnectTimeout: 100000

  ```
    
  接下来就可以在 `Application` 类中直接启动了。
  
  没有报错的话，可以访问以下路径 [localhost:8084/hi?msg=Hi](localhost:8084/hi?msg=Hi)，以检验服务是否发布成功。
  
  也可以在Nacos控制台中直观的找到该服务的信息：
  
  ![nacos-squid-example-provider.png](https://i.loli.net/2020/04/19/gUCSMWH7nylvesi.png)
  
  一切都OK了，继续下一步👇。


--- 

## SpringCloud服务订阅

 服务可以正常注册之后，我们来新建一个工程模拟消费者进行请求，如下图：
 
 ![squid-example-consumer.png](https://i.loli.net/2020/04/20/bNXx8idUvBPZcVt.png)
 
 新建的工程我们以 `squid-example-consumer` 来命名，来对应 `squid-example-provider` 的意思。
 
 照样，我们需要引入依赖：
 
 ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.yanzhenyidai</groupId>
        <artifactId>squid-example-sc-api</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
 ```
 
 依赖与 `squid-example-provider` 一致，紧接着，我们可以来编写调用 `squid-example-provider` 对外暴露的 `HiService` 接口，同样新建一个 `Application`
 的启动类：
 
 ```java
    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableFeignClients
    @RestController
    public class ScConsumerApplication {
    
        @Autowired
        private HiService hiService;
    
        @GetMapping(value = "/hi")
        public String hi(String msg) {
            return hiService.hi(msg);
        }
    
        public static void main(String[] args) {
            SpringApplication.run(ScConsumerApplication.class, args);
        }
    }
 ```
 
 `application.yaml` 文件：
 
 ```yaml
    spring:
      application:
        name: squid-example-sc-provider
      cloud:
        nacos:
          discovery:
            server-addr: yanzhenyidai.com:8848
        discovery:
          client:
            health-indicator:
              enabled: false
    server:
      port: 8085
    ribbon:
      ReadTimeout: 100000
      ConnectTimeout: 100000
 ```
 
 启动 `squid-example-consumer` 的 `Application`后，我们可以访问 `localhost:8085/hi?msg=Hi` 来进行验证，此时如果先打端点在 `squid-example-provider` 的 `HiServiceImpl` 方法实现上，可以看到程序进入到了该断点。
 
 虽然本意上 `squid-example-consumber` 工程是多此一举，毕竟现在项目大多都是前后端分离，不过通过这个消费工程也可以明白到Nacos是怎么做到的服务发现，以及如果内部服务需要相互调用，也可以这样进行。
 
 **PS：Nacos的服务发现：**
 
 ![Nacos-discovery.jpeg](https://i.loli.net/2020/04/20/H5XjLwV4sd3MYTz.jpg)
 
 上图是Nacos官网的基础机构及概念图，大致看上去逻辑和Zookeeper的调用机制类似。服务先将关键信息注册到Nacos，调用时根据协议再去请求各个服务。这里不做多提，感兴趣可以下[Zookeeper服务注册和发现]()。

---

## 总结

 SpringCloud作为近期很火的微服务框架，本篇记录的是个人搭建SpringCloud服务的一些记录，感谢您的阅读，希望能帮助到你，如果有遇到问题，可以发送邮件到[frank-tan@outlook.com](mailto:frank-tan@outlook.com)。
 
 参考资料：
 
> [SpringCloud(IO)](https://spring.io/projects/spring-cloud) <br>
> [SpringCloud(GITHUB)](https://github.com/spring-cloud-samples/)