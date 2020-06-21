
# 搭建微服务框架（集成Swagger）

 ![squid-swagger.png](https://i.loli.net/2020/06/14/OypWg9m45hrBl8Q.png)

> SQuid项目中对外接口慢慢多了起来，输出文档和调试起来有点手足无措，了解到Swagger后，打算集成到项目中来，于是就有了这篇博客。

---

## 介绍

 听说 `Swagger` 是在2019年初的时候，那个时候由于接口字段经常变更，输出文档跟不上变更的脚步，本来是想将Swagger集成到项目中，奈何基础架构太烂，有太多不兼容的地方。
 
 > 借用开源中国的Swagger介绍：

 Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。

---

## 使用

 目前在项目中还是一些简单的使用，只是将 `Controller` 层的接口加入了这一规范中。
 
 > 引入依赖
 
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.6.1</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.6.1</version>
</dependency>
```
 
 引入最新的 swagger 2.0依赖，`io.springfox`，这里我引入的是 `2.6.1` 版本。
 
> 加入Config

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yanzhenyidai.squid.invoice.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台服务接口")
                .contact(new Contact("frank-tan", "www.yanzhenyidai.com", "frank-tan@outlook.com"))
                .version("1.0")
                .description("API")
                .build();
    }
}
```

 - basePackage： 需要扫描并且展示的接口地址
 - title： 标题
 - Contact： 联系人，网站地址，邮箱
 - version： 版本
 - description： 描述
 
 
> 常用注解

  - @Api
    表示这是一个swagger的资源
  
  - @ApiOperation
    表示这是一个http的方法
  
  - @ApiModel
    表示这是一个请求实体
  
  - @ApiModelProperty
    请求实体中的参数描述
  
  - @ApiParam
    单个参数的请求的描述

> 示例

```java
@RestController
@RequestMapping(value = "/user")
@Api(value = "用户信息接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/findOne")
    @ApiOperation(value = "查询单个用户信息")
    public User findOne(@ApiParam(value = "用户信息") @RequestBody User user) {
        user.setWechatId(SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.findOne(user);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "用户信息保存")
    public User save(@ApiParam(value = "用户信息") @RequestBody User user) {
        return userService.save(user);
    }
}
```    

> 效果

 启动好项目后，在项目服务后加 `/swagger-ui.html` 可以看到效果

![swagger-preview.png](https://i.loli.net/2020/06/21/YX4paHZ9kILjzwJ.png)

---

## 总结

 Swagger在项目开发中对接口这一块有很大的帮助，将接口测试，调用以及管理变得十分简单。
 
 参考资料：
 
 > [Swagger](https://swagger.io/)