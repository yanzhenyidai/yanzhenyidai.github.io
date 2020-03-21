
# RESTFUL

<img src="https://i.loli.net/2020/03/21/yeBcjPp1Mg2zYkI.jpg" width="60%"/>

> RESTFUL已经运用好几年了，在实际项目中大部分也都有用到了这项技术，下面来讲讲自己的理解

 ---
## 介绍

   网上介绍有很多，具体可以搜索一下详细介绍，总结下来就是一套Http JAX-WS的CRUD请求规范
   
 ---
   
 ---
## 实现

   实现的方式接触过的有两种，RestEasy和Jersey，个人比较喜欢使用Jersey，不管是从Github Star数量，以及日常使用
 
> [JBoss RestEasy](https://github.com/resteasy/Resteasy)
 
> [Jersey](https://github.com/jersey/jersey)

   DUBBO中用的就是JBoss的RestEasy，做过的项目中大部分都是用到的DUBBO，在使用过程中，RestEasy最大的痛点就是没有支持文件上传的API。
   
   而Jersey中的Post，可以接收File的InputSteam，不过是作为text-html的类型。
   
   当然如果是自己想实现一个简单的Rest，使用Jersey比较好，如果已经在使用DUBBO了，还是按照DUBBO的设计来使用。
   
 ---

 ---
## 常用的注解
 
 1. GET
  获取服务提供的资源，一般用作单个字段的查询
 
 2. PUT
  更新或者添加资源，一般用作更新操作
 
 3. DELETE
  删除资源，一般用作删除操作
  
 4. POST
  添加资源，一般用作新增操作，也有一些查询的操作，比如多字段查询
  
 上面四种常见的REST请求注解，符合幂等性的有前三种，第四种Post由于做了新增操作，有可能会发起两次同样的请求。
 
   | 参数注解        | 含义                      |  示例 |
   | --------       | --------                  | -------- |
   | QueryParam     | 查询参数接收               |  /query?field=xxx&field1=xxxx |  
   | PathParam      |   单个查询查询接收         | /query/1  |
   | FormParam      |    相当于定义的表单参数    | query  |
   | BeanParam      |    自定义参数查询          |  query  |
 
 ---


