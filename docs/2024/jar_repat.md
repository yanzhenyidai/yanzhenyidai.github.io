# Jar包中替换lib下的jar文件

> 有一些老项目，涉及到安全漏洞扫描，像这种情况下有的时候代码都无处可寻，只能通过替换jar包来规避漏洞扫描信息。

---

## 替换中的问题

按照平常思路来说，都是直接使用压缩工具在线打开，删除掉低版本的jar信息，然后将新的jar拉到压缩工具中。

但是这种方法有时候会直接失败，像是Springboot中就不允许这样更改jar信息，错误日志有可能会出现：

```text
Exception in thread "main" java.lang.IllegalStateException: Failed to get nested archive for entry BOOT-INF/lib/jackson-databind-2.10.4.jar
        at org.springframework.boot.loader.archive.JarFileArchive.getNestedArchive(JarFileArchive.java:108)
        at org.springframework.boot.loader.archive.JarFileArchive.getNestedArchives(JarFileArchive.java:86)
        at org.springframework.boot.loader.ExecutableArchiveLauncher.getClassPathArchives(ExecutableArchiveLauncher.java:70)
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:49)
        at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51)
Caused by: java.io.IOException: Unable to open nested jar file 'BOOT-INF/lib/jackson-databind-2.10.4.jar'
        at org.springframework.boot.loader.jar.JarFile.getNestedJarFile(JarFile.java:256)
        at org.springframework.boot.loader.jar.JarFile.getNestedJarFile(JarFile.java:241)
        at org.springframework.boot.loader.archive.JarFileArchive.getNestedArchive(JarFileArchive.java:103)
        ... 4 more
Caused by: java.lang.IllegalStateException: Unable to open nested entry 'BOOT-INF/lib/jackson-databind-2.10.4.jar'. It has been compressed and nested jar files must be stored without compression. Please check the mechanism used to create your executable jar file
        at org.springframework.boot.loader.jar.JarFile.createJarFileFromFileEntry(JarFile.java:284)
```

---

## 解决方法

![7z.png](https://s2.loli.net/2024/08/08/vBQomj1NyI5l6dK.png)

这种时候将jar文件解压缩，然后将lib下的文件都进行替换，然后再压缩，选择压缩工具中的`仅压缩`，压缩之后的文件可以正常运行。