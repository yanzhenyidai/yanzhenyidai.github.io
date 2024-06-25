# Mysql迁移Pgsql （未完）

![pgsql.jpeg](https://s2.loli.net/2024/06/25/V6moaTzbQOIPBWX.jpg)

> 最近可能会做信创，涉及到国产服务器操作系统、数据库等，由于Java是跨平台的语言，操作系统目前都是基于Linux，差别不太大，主要是数据库，国产数据库大多数是基于PostgreSQL进行魔改，
而且这一块适配比较难，主要在于数据迁移部分。

---

## Pgloader

[Pgloader](https://github.com/dimitri/pgloader) 是一个用于从MySQL到PostgreSQL的数据迁移工具，可以支持多种数据格式，包括CSV、JSON、XML等。

1. 安装pgloader：

这里是基于源码安装，将github上的pgloader插件代码clone到服务器上。然后对源码中的`centos7.sh`进行授权，然后执行`centos7.sh`安装。

```shell
git clone https://github.com/dimitri/pgloader.git
cd pgloader
chmod -R 775 bootstrap-centos7.sh
./bootstrap-centos7.sh
make pgloader
cd build/bin
```

此时会生成pgloader可执行文件，这里我们基于pgloader进行下一步处理。

2. 执行

```shell
./pgloader -d mysql://user:password@localhost:3306/test pgsql://user:password@localhost:5432/test
```

这里目前还存在问题，还在研究，一直提示pgsql 28P01的密码错误提示。

