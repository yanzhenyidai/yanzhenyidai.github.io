# 造一个轮子，实现RPC调用

 > 在写了一个Netty实现通信的简单例子后，萌发了自己实现RPC调用的想法，于是就开始进行了Netty-Rpc的工作，实现了一个简单的RPC调用工程。
  <br>
 > 如果也有兴趣动手造轮子的同学，可以先看看之前写的 [使用Java实现Netty通信](https://yanzhenyidai.com/#/netty/use-java-create-netty-communication) 这篇博客。

---

## 准备

 首先，你需要明白下列知识。
 
 > Netty
 
 处理服务之间的通信。
 
 > Zookeeper
 
 服务注册与发现。
 
 > SpringBoot
 
 目前单单只是作为启动项目。
 
 > Cglib Proxy & Jdk Reflect 
 
 使用代理实例化暴露的接口，通过反射调用接口方法的实现。

---

## RPC处理流程

 ![Rpc process.jpg](https://i.loli.net/2020/05/28/kyIRKz3UaATwmcu.png)

 > 1. 服务提供者通过Netty进行对应的端口暴露。
 <br>
 > 2. 同时提供者将需要暴露的服务信息注册到Zookeeper，Zookeeper注册的节点信息为接口的类路径，注册的Data为暴露的端口，并且需要将对应的接口实现类在ApplicationContext上下文中进行保存。
 <br>
 > 3. 此时消费者启动后，会根据对应接口的类路径在Zookeeper进行Discover。
 <br>
 > 4. 根据对应接口的类路径在Zookeeper中通过ReadData获取到对应暴露的端口信息。
 <br>
 > 5. 拿到了端口信息，通过Netty发起请求。
 <br>
 > 6. Netty发起请求接收后，通过之前的ApplicationContext上下文调用对应的接口方法。
 <br>
 > 7. ApplicationContext调用成功后，Netty将响应的结果返回。
 <br>
 > 8. 服务消费者得到响应结果，RPC流程结束。
 
---

## 动手
 
 在 [使用Java实现Netty通信](https://yanzhenyidai.com/#/netty/use-java-create-netty-communication) 这篇文章的基础上，我们新建下列工程信息。

 ![netty-rpc-sample.png](https://i.loli.net/2020/05/28/qS9JZcHn1KfhRDe.png)
 
 ### rpc-sample-api
 
 > 依赖信息
 
```xml
<dependency>
    <groupId>com.yanzhenyidai</groupId>
    <artifactId>netty-rpc-common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
 
 > 代码
 
 只需要定义一个HiService接口。
 
```java
public interface HiService {

    public String hi(String msg);
}
```
 
 ### rpc-sample-server
 
 > 依赖信息
 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.0.3.RELEASE</version>
</dependency>

<dependency>
    <groupId>com.yanzhenyidai</groupId>
    <artifactId>rpc-sample-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.yanzhenyidai</groupId>
    <artifactId>netty-rpc-server</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
 > application.yaml
 
```yaml
register.address: 127.0.0.1:3000
```

 > 代码
 
 - 首先实现HiService接口。
 
```java
@RpcServer(cls = HiService.class)
public class HiServiceImpl implements HiService {

    public String hi(String msg) {
        return "hello, I'm Rpc, I want say : " + msg;
    }
}
```

 - Server类。
 
```java
@Component
public class Server implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Value("${register.address}")
    private String registerAddress;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBean = new HashMap<String, Object>();

        Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(RpcServer.class);

        for (Object object : objectMap.values()) {
            try {
                RpcServer annotation = object.getClass().getAnnotation(RpcServer.class);

                serviceBean.put("/yanzhenyidai/" + annotation.cls().getName(), object);

                String[] split = registerAddress.split(":");

                new NettyServer(split[0], Integer.valueOf(split[1])).server(serviceBean);
            } catch (Exception e) {
                logger.error("[server-start] fail ", e);
            }
        }
    }
}
```

 实现 `ApplicationContextAware` 以获取到Spring上下文，通过扫描RpcServer注解，得到本次需要暴露的服务信息，并且开启NettyServer的端口服务暴露及Zookeeper注册。
 
 - Application启动类

```java
@SpringBootApplication
public class RpcServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RpcServerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
```

 开启SpringBoot无Web启动。

  
 ### rpc-sample-client
 
 > 依赖
 
```xml
<dependency>
    <groupId>com.yanzhenyidai</groupId>
    <artifactId>netty-rpc-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.yanzhenyidai</groupId>
    <artifactId>rpc-sample-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.2.2</version>
</dependency>

<dependency>
    <groupId>asm</groupId>
    <artifactId>asm</artifactId>
    <version>3.3.1</version>
</dependency>
```

 > 代码
 
 - Client
 
```java
public class Client {

    public <T> T create(final Class<?> cls) {

        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new InvocationHandler() {
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

                Request request = new Request();
                request.setInterfaceName("/yanzhenyidai/" + cls.getName());
                request.setRequestId(UUID.randomUUID().toString());
                request.setParameter(objects);
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());

                Response response = new NettyClient().client(request);
                return response.getResult();
            }
        });
    }
}
``` 
 
 使用Cglib动态代理先实例化接口信息，在调用的时候，通过NettyClient发送请求到NettyServer，由NettyServer处理发现Zookeeper节点以及反射调用接口实现方法。
 
 - context.xml
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="com.yanzhenyidai.config.Client"></bean>

</beans>
```

 注入Client的bean对象信息。
 
 - Application
 
```java
public class RpcClientApplication {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        Client client = context.getBean(Client.class);

        HiService hiService = client.create(HiService.class);
        String msg = hiService.hi("msg");
        System.out.println(msg);

    }
}
```

 > 运行结果
 
 ![rpc-sample-result.png](https://i.loli.net/2020/05/28/SN736Ek1DMtchze.png)
 
---

## 总结

 以上只是一个简单的RPC过程，相对于Dubbo RPC会更加直观的看明白，希望能对大家有所帮助作用，也欢迎大家和我一起造轮子。😝
 
 本项目Github地址：[Netty-RPC](https://github.com/yanzhenyidai/netty-rpc-example)