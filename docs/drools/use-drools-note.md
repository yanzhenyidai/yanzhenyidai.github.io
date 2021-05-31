
# Drools使用心得

![droos.jpg](https://i.loli.net/2021/04/28/VgrRIQ9POZWHa5x.jpg)

> 换了份工作，接触到了Drools，有一丝丝的相见恨晚的赶脚，之前公司做产品以及写代码的时候，规则一般都是直接再项目中写了一个常量，然后读对应的常量信息做规则校验，<br>
这样的痛点就是每次新加一个规则就要重新部署一次，比较麻烦，接触到了Drools，感觉以后做类似项目应该也能集成，这里做下使用Drools的记录。

---

## 介绍
 
 介绍就不多进行讲了，网上随便一搜，基本上都能知道这个工具是干什么用的，总结一下就是动态规则校验工具，本项目的Git地址：[drools-example](git@github.com:yanzhenyidai/drools-example.git)

---

## 使用

 我们这次使用基于一个简单的demo，demo使用SpringBoot进行搭建，集成SpringDataJPA，做到动态加载规则的实现，下面进行使用记录。
 
### 本次Drools的实现版本为6.5.0，SpringBoot版本为2.0.4

 首先新建项目，引入drools的jar包，以及SpringBoot相关所需要的jar包依赖。

```xml
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring.version>2.0.4.RELEASE</spring.version>
        <drools.version>6.5.0.Final</drools.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <type>pom</type>
                <version>${spring.version}</version>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
            <version>2.0.4.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.68</version>
        </dependency>

        <dependency>
            <groupId>org.kie</groupId>
            <artifactId>kie-spring</artifactId>
            <version>${drools.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-tx</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-beans</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-context</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.drools</groupId>
            <artifactId>drools-compiler</artifactId>
            <version>${drools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.3.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### drools的主要实现类，以及drl文件相关语法

> KieContainer

> KieServices

> KieRepository

> KieFileSystem

### 处理流程

1. 添加各个规则字段信息以及对应值
2. 根据规则字段信息生成Drools.drl文件，并加载到KieContainer容器中
3. 保存到数据库
4. 调用时，根据生成的RuleKey信息，使用 `kieSession.fireAllRules(new RuleNameEqualsAgendaFilter(request.getRuleKey()));` 进行单个规则的触发
5. 根据触发后的响应值，进行错误信息提示

---

## 总结

写的比较浅，确实一些小的项目基本上都是自己写的一套逻辑代码进行规则校验，使用规则引擎的判断项目少之又少，这个从网络上的中文参考教程数量上就能体现出来。

参考资料：

> [drools.org](https://www.drools.org/) <br>
> [drools各规则语法参考](https://www.cnblogs.com/jpfss/p/12028041.html)
 