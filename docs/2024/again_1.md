# 重启2024🥇🥇🥇

![wiki.png](https://s2.loli.net/2024/03/13/hVgqG2YMsu1cFHB.png)

> 上图是我的WIKI提交记录，可以看到上一次提交记录还是在2021年5月份，那个时候正在上汽通用上班，难得也是有一位老韩一样的好领导，允许我们工作
完成之后可以做自己的事情，于是写了一篇关于DROSS的博客。

## 前言

说来惭愧，自从回到长沙后，似乎没有了在上海那么好的习惯，没有写博客，没有继续跑步，也没有说有到YOUTUBE上看国外的资讯，也没有到Medium上看英文文章，正巧这段时间
长沙所在的公司也卷了起来，想考虑一些关于自己往后的发展。的的确确这两年来也是吃老本不断。于是想完成几年前的心愿，想搭一套轻量级，不冗余的平台出来，也为自己往后的发展
做铺垫，毕竟以前用的老东家那套不好拓展，也不好维护，去年只是改了界面，做了一次小升级。

---

## 规划

关于后续规划，我的计划还是沿用DUBBO这套框架，使用它的Dubbo-SpringBoot-Stater版本，两三年前用的那一套还是0.1的版本，现阶段的2.7.8版本很符合我的方向，初步搭出来的一套Demo结构如下：

- Dubbo-CLI（总体工程）
    - Dubbo-Parent（管理所有maven依赖）
    - Dubbo-Common（管理所有通用类信息）
    - Dubbo-Business（业务实现）
        - Dubbo-Business-API （对内的业务接口）
        - Dubbo-Business-Service （对内的业务接口实现，做为持久化实现）
    - Dubbo-Backend（对外提供的HTTP接口）

如图：

![dubbo-cli.png](https://s2.loli.net/2024/03/13/UdqxhrQGJcEYtwb.png)

> 整体逻辑：

对内服务全由Dubbo进行RPC调用，外部和前端交互由Backend模块使用Spring-Boot-Starer-Web进行交互。所有接口都走Backend，使用Jwt进行鉴权。

---

## 技术选型

技术选型后端还是用的Spring全家桶。

1. [ ] Dubbo-Spring-Boot-Starer（2.7.8）
2. [ ] SpringDataJPA
3. [ ] SpringDataMongo（存图片和json结构化数据）
4. [ ] Spring（5.2.8）
5. [ ] JWT（Token）

前端选型则为Element-Plus，这块可能需要找外援，目前想法是先将后端服务从开发到部署流程打通。

---

## 时间安排

此次由于只能在家里开发，想法是有时间就干，或者后续在家期间来进行，总之，此次不求别的，只为前几年未完成的一次心愿。加油💪。

--- 

## 其他

1. [ ] [SpringBoot-Shrio-Jwt参考](https://gitee.com/fugongliudehua/shiroJWT.git)