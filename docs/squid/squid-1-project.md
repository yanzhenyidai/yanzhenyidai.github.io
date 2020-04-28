
# 搭建微服务框架（结构和各个组件）

---

## 简介

 ![squid.png](https://i.loli.net/2020/04/15/YwiSDkh2JVfR6Zz.png)

> SQuid是基于Spring，SpringBoot，使用了SpringCloud下的组件进行构建，目的是想搭建一套可以快速开发部署，并且很好上手的一套微服务框架。

### 组件
- [x] [Spring-Cloud-Feign]()
- [x] [Spring-Security-OAuth](https://blog.yanzhenyidai.com/2019/08/15/Spring-Security-Oatuh2/)
- [x] Spring-Cloud-Gateway
- [x] Spring-Cloud-Alibaba

### 中间件
- [x] [Redis]()
- [x] Nacos
- [ ] Sentinel
 
### 数据库持久层
- [x] [SpringDataJPA]()
- [x] Mybatis-Plus

### 数据库
- [x] Mysql5.7 +
- [ ] Oracle
 
---

## 环境

- [x] JDK1.8
- [x] Maven3.2.5
- [x] Idea


 如果你的电脑上已经有安装好上面的程序，那么你可以打开git，输入命令 `git@github.com:yanzhenyidai/squid.git` 将本项目克隆到本地运行。
 
 不过也可以先看看后面各个组件的集成的说明，送上链接：
 
 - [SC服务注册与发现]()
 - [读取Nacos的配置信息]()
 - [服务接口鉴权]()
 - [服务网关处理]()
 - [数据库持久层-SpringDataJpa]()
 - [数据库持久层-Mybatis-Plus]()
 - [服务熔断和跟踪-Sentinel]()
 
---

## 项目依赖

 > 本次项目是在 `spring-boot: 2.0.9.RELEASE` 下搭建，所需依赖文件如下：
 
 ```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.9.RELEASE</version>
    </parent>
    
    <properties>
        <spring-cloud-alibaba.version>2.1.0.RELEASE</spring-cloud-alibaba.version>
        <spring-cloud-openfeign.version>2.2.0.RELEASE</spring-cloud-openfeign.version>
        <spring-cloud.version>Finchley.SR2</spring-cloud.version>
        <druid-starter.version>1.1.21</druid-starter.version>
    </properties>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid-starter.version}</version>
            </dependency>

            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-openfeign-dependencies</artifactId>
                <version>${spring-cloud-openfeign.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
 ```
 
  parent以 `springboot` 开始，原因为 springcloud是基于 springboot 的一套脚手架工具，在搭建本项目时遇到了不少的版本冲突的问题，可以看一下[springboot和springcloud版本整理](https://blog.yanzhenyidai.com/2020/01/16/SpringBoot，SpringCloud各版本比对/)。
 
 希望能对你有到帮助。
