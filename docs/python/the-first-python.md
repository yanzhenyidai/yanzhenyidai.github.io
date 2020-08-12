
# 人生苦短，我用Python

> 最近查看了自己的服务器上Nginx的日志，发现有一些国外的IP经常访问，估摸着是搜索引擎爬虫，于是想着分析下Nginx的access.log文件，获取所有的IP信息，
然后保存到数据库，出具图形界面来展示。

<br>

> 刚开始打算是用Java来实现，结果没想到Java的正则一直匹配不到IP信息，后来想到了Python，记得它比较适合写爬取脚本，于是开始了 `人生苦短，我用Python`
的故事。

![python.jpg](https://i.loli.net/2020/08/12/i2nFvhfp7gblDcq.jpg)

---

## 了解Python

 Python是在前几年的时候，出来找工作，有HR问愿不愿意考虑Python的岗位，当时主要学的是Java，如果换行就意味着需要重新开始，当时胆子小，结果走向了Java这条不归路。
 
 Python前几年给我的印象是，爬虫、服务器脚本语言。
 
 现在写起来之后，发现Python真的太简单了，当然也有原因是只是自己写的Python代码是基础，真正如果使用Python写项目的话，估计还是有点难吃。

 不过在熟悉了Python之后，感觉GO语言有点像是 Java 和 Python 的结合体。
 
---

## 第一段Python代码

```python
import requests
import pymysql
import time
import random

conn = pymysql.connect(
    host='localhost',
    port=3306,
    user='python',
    password='python,123',
    db='ip_record',
    charset='utf8'
)

if __name__ == '__main__':

#    读取access.log，使用正则表达式提取IP
#    filename = "/usr/local/nginx/logs/access.log"
#    with open(filename, 'r') as f:
#        result = re.findall(r"\b(?:[0-9]{1,3}\.){3}[0-9]{1,3}\b", f.read())
#        print(result)

    filename = "C:/Users/Admin/Desktop/access.txt"

    with open(filename, 'r') as f:

        result = f.readlines()
        print(result)

    for IP in result:
        print(IP.strip())

        url = "https://apis.juhe.cn/ip/Example/query.php?IP=" + IP

        time.sleep(random.randint(0, 3))

        d = requests.post(url).json()
        print(d)

        cursor = conn.cursor()

        if (d['error_code'] == 200800):
            sql = 'insert into nginx_access_juhe(ip,code,message,success) values (%s,%s,%s,%s)'

            rows = cursor.execute(sql,(IP,d['error_code'],d['reason'],0))
        else:
            sql = 'insert into nginx_access_juhe(ip,country,province,city,code,message,success,isp) values (%s,%s,%s,%s,%s,%s,%s,%s)'

            rows = cursor.execute(sql, (
                IP, d['result']['Country'], d['result']['Province'], d['result']['City'], d['resultcode'], d['reason'], 1,
                    d['result']['Isp']))

        conn.commit()
        cursor.close()

    conn.close()

```

---

## 对比Java

 如果拿Python和Java来对比，Python语法简单了不少。
 
 Java是需要引用JAR包，Python是需要pip install 相关的依赖库。
 
 但是Python依赖库只要安装好之后，所有的项目都能使用，而Java的JAR包还需要一个一个项目的引用。
 
 Python的语法对比Java的语法，看上面的这段代码，如果是用Java写的话，估计最起码需要200行代码左右。 

---

## 总结

 Python简单易学，不过目前对于个人来说，Python写写小工具，脚本还是不错的，如果对于项目来说，考虑熟悉程度还有部署流程，还是选择Java。
 
 参考资料：
 
 > 🌏 [Python菜鸟教程](https://www.runoob.com/python/python-tutorial.html)