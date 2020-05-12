
# 搭建微服务框架（数据库持久层-SpringDataJPA）

 ![springdatajpa.png](https://i.loli.net/2020/05/12/bN3cpgJ2z6eWnYI.png)

> 用惯了Mybatis，这次来换换口味，在SQuid中集成SpringDataJPA。

---

## 介绍
 
 以前都是听说过的是 `HibernateJPA` ，却从来没有使用过，一直在项目中使用的是 `Mybatis`。
 
 SpringDataJPA是基于Hibernate的底层封装的一套ORM框架，使用起来的第一感觉是代码量真的很少，相较传统的Mybatis来说，感觉最起码少了60%，当然大部分都是体现在xml文件上。
 
 介绍真的没有太多词汇可以展示出来，下面来进行使用。👇
 
---

## 使用

 在squid项目中，我们新建一个 `squid-example-jpa`的项目（由于之前的example目录被删除，可以根据下面的层级目录来进行新建）
 
 ![squid-jpa.png](https://i.loli.net/2020/05/12/2iAlueWoQH1BjNF.png)
 
 > 引入依赖：
 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

 
 > 生成Java实体

 如果使用的是IDEA，完全可以参考这篇博客 [IDEA下生成SpringDataJPA的Java实体](https://blog.yanzhenyidai.com/2020/05/11/IDEA%E4%B8%8B%E7%94%9F%E6%88%90SpringDataJPA%E7%9A%84Java%E5%AE%9E%E4%BD%93/)
 来生成实体信息。
 
 因为使用了lombok，所以在生成的实体中并没有 `getter setter` 方法呈现，关于lombok可以了解一下 [Lombok](../java/lombok.md)
 
 > DAO
 
 生成了实体信息后，DAO文件就需要我们自己来手工生成了：
 
```java
public interface EdocInvoiceRepository extends JpaRepository<EdocInvoice, Long> {

}
```

 一般我们直接继承的是 `JpaRepository` ，这个是包含所有JPA处理的类，基本上拥有了所有持久层的交互方法。
 
 JPARespository：
 
```java
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll();

    List<T> findAll(Sort var1);

    List<T> findAllById(Iterable<ID> var1);

    <S extends T> List<S> saveAll(Iterable<S> var1);

    void flush();

    <S extends T> S saveAndFlush(S var1);

    void deleteInBatch(Iterable<T> var1);

    void deleteAllInBatch();

    T getOne(ID var1);

    <S extends T> List<S> findAll(Example<S> var1);

    <S extends T> List<S> findAll(Example<S> var1, Sort var2);
}

```

 如果你还有其他需求，比如需要根据某两个字段进行查询等等，SpringDataJPA完全支持：
 
 
|Keyword				|Sample															|PQL snippet|
|------------- 			| ------------- | --------------------------------------------------------|
|And				|findByLastnameAndFirstname								 		|where x.lastname = ?1 and x.firstname = ?2                              |
|Or					|findByLastnameOrFirstname								 		|where x.lastname = ?1 or x.firstname = ?2                               |
|Is,Equals			|findByFirstname,findByFirstnameIs,findByFirstnameEquals	… 	|	where x.firstname = ?1                                               |
|Between			|findByStartDateBetween	… 										|where x.startDate between ?1 and ?2                                     |
|LessThan			|findByAgeLessThan	… 											|where x.age < ?1                                                        |
|LessThanEqual		|findByAgeLessThanEqual		… 									|where x.age <= ?1                                                       |
|GreaterThan		|findByAgeGreaterThan	… 										|where x.age > ?1                                                        |
|GreaterThanEqual	|findByAgeGreaterThanEqual	… 									|where x.age >= ?1                                                       |
|After				|findByStartDateAfter	… 										|where x.startDate > ?1                                                  |
|Before				|findByStartDateBefore	… 										|where x.startDate < ?1                                                  |
|IsNull				|findByAgeIsNull	… 											|	where x.age is null                                                  |
|IsNotNull,NotNull	|findByAge(Is)NotNull	… 										|where x.age not null                                                    |
|Like				|findByFirstnameLike	… 										|	where x.firstname like ?1                                            |
|NotLike			|findByFirstnameNotLike	... findByFirstnameNotLike              |                                                                        |
|StartingWith		|findByFirstnameStartingWith	… 								|	where x.firstname like ?1 (parameter bound with appended %)          |
|EndingWith			|findByFirstnameEndingWith	… 									|where x.firstname like ?1 (parameter bound with prepended %)            |
|Containing			|findByFirstnameContaining	… 									|where x.firstname like ?1 (parameter bound wrapped in %)                |
|OrderBy			|findByAgeOrderByLastnameDesc	… 								|where x.age = ?1 order by x.lastname desc                               |
|Not				|findByLastnameNot	… 											|where x.lastname <> ?1                                                  |
|In					|findByAgeIn(Collection<Age> ages)	… 							|where x.age in ?1                                                       |
|NotIn				|findByAgeNotIn(Collection<Age> ages) … 						|	where x.age not in ?1                                                |
|True				|findByActiveTrue()	… 											|where x.active = true                                                   |
|False				|findByActiveFalse()	… 										|	where x.active = false                                               |
|IgnoreCase			|findByFirstnameIgnoreCase	… 									|where UPPER(x.firstame) = UPPER(?1)                                     |

 也可以访问SpringDataJPA的官方文档查看：[SpringDataJPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions)
 
 
 > Service & Impl & Controller
 
 service、service.impl，controller，按照常规项目中来编写就行，其中需要注意的是，service如果是要作为对外接口，可以注明 `@FeignClient("squid-example-jpa")`，可以参考 [sc-server](squid-2.1-sc-server.md)
 
 贴上项目中的例子：
 
```java

public interface EdocInvoiceService {

    List<EdocInvoice> findAll();

    EdocInvoice save(EdocInvoice edocInvoice);
}

@Service
public class EdocInvoiceServiceImpl implements EdocInvoiceService {


    @Autowired
    private EdocInvoiceRepository edocInvoiceRepository;

    @Override
    public List<EdocInvoice> findAll() {
        return edocInvoiceRepository.findAll();
    }

    @Override
    public EdocInvoice save(EdocInvoice edocInvoice) {
        return edocInvoiceRepository.save(edocInvoice);
    }
}

@RestController
@RequestMapping(value = "/invoice")
public class EdocInvoiceController {

    @Autowired
    private EdocInvoiceService edocInvoiceService;

    @PostMapping(value = "/findAll")
    public List<EdocInvoice> findAll() {
        return edocInvoiceService.findAll();
    }

    @PostMapping(value = "/save")
    public EdocInvoice save(@RequestBody EdocInvoice edocInvoice) {
        return edocInvoiceService.save(edocInvoice);
    }
}

```


 > application.yaml

```yaml
spring:
  application:
    name: squid-miniprogram
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://yanzhenyidai.com:3306/fapiaochi?useUnicode=true&characterEncoding=utf-8
    username: root
    password: ***
    type: com.alibaba.druid.pool.DruidDataSource
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 9090
```

 `jpa.hibernate.ddl-auto` ：

|key             | value                                |
|---------       |----------------------------------------|
|create          |启动时删数据库中的表，然后创建，退出时不删除数据表|
|create-drop     |启动时删数据库中的表，然后创建，退出时删除数据表 如果表不存在报错|
|update          |如果启动时表格式不一致则更新表，原有数据保留|
|validate        |项目启动表结构进行校验 如果不一致则报错|

 一般的 `ddl-auto` 属性使用 `update` 就行。
 
 > 多表查询
 
 复杂业务出现时，可能会使用到多表查询，但是JPA不像Mybatis那么灵活，需要创建中间实体来进行接收，我一般的做法两张表分开查询，然后合并。
 
---
 
 ## 总结
 
 SpringDataJPA使用起来的感受就是一个字 “**快**” ，相较于比较小型的项目，真的推荐使用SpringDataJPA，它能快速的搭建起一个持久层，也不用像Mybatis一样太多的关心底层的xml文件。
 
 参考资料：

 >[SpringDataJPA-Github](https://github.com/spring-projects/spring-data-jpa) <br>
 >[SpringDataJPA-doc](https://docs.spring.io/spring-data/jpa/docs/2.3.0.RELEASE/reference/html/#reference)
 

