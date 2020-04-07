
# SpringBoot

 ![springboot.png](https://i.loli.net/2020/04/01/GORdxJWf7q1oTIe.png)

> SpringBoot作为近几年很火的微服务框架，只需要简单的几个依赖，少量的配置，就可以使用它快速搭建一个轻量级的微服务，优点是简单、快速、大道至简，缺点是真的太单一，不适于项目中的模块开发。<br/>
 如果是单一的应用，比如做接口转发、项目启动，SpringBoot很合适这些场景，如果是项目开发，建议还是使用[SpringCloud](springcloud.md)。<br/>
 下面整理了一些理解SpringBoot和使用SpringBoot的内容，Spring官方说明请点击[SpringBoot Starter](https://spring.io/projects/spring-boot)。

## 理解SpringBoot

> 基本注解

- SpringBootApplication

> 加载类

- TypeExcludeFilter
- AutoConfigurationExcludeFilter
- EnableAutoConfiguration
- AutoConfigurationImportSelector

> Web服务

- WebServer

> 加载顺序，淡黄色为注解，淡绿色为类。

 ![SpringBoot 加载.jpg](https://i.loli.net/2020/04/07/bWFlJAkx3EgZvz6.png)
 
 按照图上的顺序，并且跟随SpringBoot的源码来进行断点跟踪，其实不难发现，SpringBoot加载顺序的逻辑意义：
 
 ```java
    @SpringBootApplication
    public class Application {
    
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    
    }

```
 
 ### SpringBootApplication
 
  在启动类上标记的 `SpringBootApplication` 注解，表明这是一个SpringBoot项目， 而 `SpringApplication.run(Application.class, args);`，作为SpringBoot的启动，也进入到图上的Initializer流程中；
  
 ### Initializer
 
  在Initializer中，主要的也就是 `SpringApplication` 类中，初始化加载了一些参数：
  
 ```java
    public SpringApplication(ResourceLoader resourceLoader, Class... primarySources) {
            this.sources = new LinkedHashSet();
            this.bannerMode = Mode.CONSOLE;
            this.logStartupInfo = true;
            this.addCommandLineProperties = true;
            this.headless = true;
            this.registerShutdownHook = true;
            this.additionalProfiles = new HashSet();
            this.isCustomEnvironment = false;
            this.resourceLoader = resourceLoader;
            Assert.notNull(primarySources, "PrimarySources must not be null");
            this.primarySources = new LinkedHashSet(Arrays.asList(primarySources));
            this.webApplicationType = WebApplicationType.deduceFromClasspath();
            this.setInitializers(this.getSpringFactoriesInstances(ApplicationContextInitializer.class));
            this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
            this.mainApplicationClass = this.deduceMainApplicationClass();
        }
        
```

  大概在类中的第92行左右，初始化了log，initializer，listener，包括resources下的配置文件：application.yaml；
  
 ### Scan
 
  在 `TypeExcludeFilter` 和 `AutoConfigurationExcludeFilter` 里，带来的最主观的应该就是basepackage的scan了，像是为下个动作进行package的路径。
  
  这里的逻辑很像Spring的设计风格，AOP，首先定义的切面，将绝对的package路径下的class进行扫描，得到对应的切点，得到切点了后面该怎么办？ :point_down:
 
 ### Configuration
 
  得到了切点了，当然是进行IOC啦，都拿到了class，不进行控制反转还要干什么呢，运用java中的反射机制，将这些class进行实例化。
  
  在 `AutoConfigurationImportSelector` 中，获取到的class：
  
  ![configuration.png](https://i.loli.net/2020/04/01/h6bayYjLAtgXHU9.png)
   
  后续就在 `ConfigurationClassPostProcessor` 中 `this.reader.loadBeanDefinitions(configClasses);` 进行实例化；
 
 ### Web
 
  Web，主要是SpringBoot中Restful所需要的容器，Web可以提供对外的Http请求。SpringBoot默认的WebServer是Tomcat，可以自己在项目的配置文件中更换成jetty或者netty。
  
  具体源码可以看看 `TomcatWebServer`。


 
## 使用SpringBoot

 构建SpringBoot应用超级简单，可以在[Spring Initializer](https://start.spring.io)中创建，将创建后的zip文件download到本地。
 
 或者直接在idea中 new project，选择Spring Initializer。
 
 SpringBoot发布的服务可以使用 `Resttemplate` 来进行调用，对于SpringBoot来说很方便，但是如果要传输的参数较多，还是建议看看[SpringCloud](springcloud.md)。 :smile:
 

