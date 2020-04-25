
# 搭建微服务框架（读取Nacos的配置信息）

> 本篇文章来记录下使用Nacos进行远程配置文件读取的操作，类似于 `SpringCloud-Config` 组件的功能

---
## 介绍

 Nacos不仅仅只具备服务注册发现功能，它同时也具备远程动态读取配置文件的功能。

 如果你认为这个功能没什么用，那么就真的大错特错了，举例：
 
 - 一些关键性的配置项
 
 拿我当前公司的项目上来举例，一些服务的调用时间，我们还是写在项目上的 `properties` 文件中，像企业级应用，我们设置的服务调用时间在一部分对外的接口上会出现超时的情况，
 这个时候，如果可以直接在 `Nacos Config` 上进行修改，效率也会提升不少。
 
 - 数据库配置信息
 
 以Mysql的连接池配置来说，如果配置文件全部都写在项目的 `resource` 目录下，万一代码泄露或者被某些想要报复社会的人拿到，后果的话，大家都懂的，
 绝对的 `Welcome to 51Job`。

 
---

## 使用

 我们这次直接在上篇文章中搭建的 `squid-example-provider` 中来实现，一如既往的开始第一步，引入依赖文件：
 
 ```xml
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
 ```
 
 在 `squid-example-provider` 中引入 `Nacos Config` 依赖后，我们开始来进行下一步的操作。
 
 - Nacos新建配置：
 
 登入Nacos控制台，进入配置管理，配置列表，新增一个配置：
 
  Key       |  Value
  ---       | ---
  Data ID   | 由项目中的 `bootstrap.properties` 指定。
  Group     | 分组信息，可以自己填写。
  标签      | N/A
  归属应用  | 归属的应用的信息，可以自己填写。
  描述      | 本次配置的描述。
  配置格式  | 根据自己项目需求来选择。
  配置内容  | 对应配置格式的配置文件。
 
 ![nacos-configuration.png](https://i.loli.net/2020/04/22/jJFtcOv9CMTd3lI.png)
 
 - 项目resource下新建 `bootstrap.properties`
 
 完成Nacos的新建配置后，我们这个时候可以来到项目中新建一个 `bootstrap.properties` 文件，之前的 `application.yaml` 文件可以删除掉了，之所以命名为 `bootstrap.properties`，是因为SpringCloud的加载配置顺序优先级properties文件大于yaml。
 
   Key                                          |  Value
   ---------------------------------------      | ----------------------------------
   spring.profiles.active                       | 配置文件的属性，比如上面Nacos里的Data ID是以-test结尾，这里我们就写 test。
   spring.application.name                  | 应用名称，写项目名就好了
   spring.cloud.nacos.config.file-extension      | 加载的配置文件格式。
   spring.cloud.nacos.config.server-addr    | Nacos的地址。
 
 ```properties
  spring.profiles.active=test
  spring.application.name=squid-example-provider
  spring.cloud.nacos.config.file-extension=yaml
  spring.cloud.nacos.config.server-addr=yanzhenyidai.com:8848
 ```
 
 - 启动项目
 
 配置完成后，我们可以启动 `Application` 类，来检验是否可以成功读取到配置文件信息。

---

## 总结

 Nacos的Config配置功能真的很方便，而且支持热加载形式，感兴趣的朋友可以更深层次的了解。
 
 参考资料：
 
 > [Nacos(GITHUB)](https://github.com/alibaba/nacos) <br>
 > [Nacos(WIKI)](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-config)

