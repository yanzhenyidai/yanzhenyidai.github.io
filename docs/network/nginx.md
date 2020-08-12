
# Nginx

> Nginx作为一款反向代理服务器，现在大多数网站都有使用，自己在项目中几乎都有用到，自己的网站也使用到了它。

---
## 了解Nginx

 ![nginx.png](https://i.loli.net/2020/04/07/t2hgMHnsDdfVbcm.png)
 
 上面图可以直观的看出Nginx的用处，可以将请求转发至Web服务器和文件服务器，当然还可以转发其他的内容，比如Rest接口，TCP端口等等。
 
---

---
## 使用Nginx

 Nginx安装成功后，大部分内容都是配置在nginx.conf文件中。

> ### 负载均衡
 
  如果是在同一台服务器上运行了多个Web服务，需要做负载均衡，首先只需要在nginx.conf中配置 `upstream` 参数，设置好所有的服务请求路径，为这些服务配置好权重以及其他连接参数。
  
  ```
   upstream tuling {
       server 127.0.0.1:8050    weight=1  max_fails=1  fail_timeout=20;
       server 127.0.0.1:8060    weight=1;
   }
  ```
  
  这样就可以通过 `server` 参数来监听指定端口，通过 `location` 来转发到设置好的负载均衡服务上。
  
 > ### 设置HTTPS
  
  Nginx设置HTTPS十分简单，HTTPS端口一般为443端口，我们只需要监听443，配置好HTTPS证书，重启Nginx服务后立刻就会生效。
  
  ```
  server {
      listen       443;
      server_name  yanzhenyidai.com;

      ssl on;
      ssl_certificate      /usr/local/nginx/cert/www.yanzhenyidai.com.pem;
      ssl_certificate_key  /usr/local/nginx/cert/www.yanzhenyidai.com.key;

      ssl_session_cache    shared:SSL:1m;
      ssl_session_timeout  5m;

      ssl_ciphers  HIGH:!aNULL:!MD5;
      ssl_prefer_server_ciphers  on;

      location / {
          #proxy_pass      http://localhost:4000;
          proxy_pass      http://localhost:3000;
          client_max_body_size 3000m;
          proxy_http_version 1.1;
          proxy_redirect     off;
          proxy_set_header Host $http_host;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection 'upgrade';
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;

      }
  }

  ```
  
  而对外开放的80端口，通常是使用 `rewrite` 转发到监听的443端口，这样无论怎么请求都是走的HTTPS。

> ### Nginx监听指定域名
 
 Nginx也可以做到监听指定的域名，通过配置 `server_name` 来处理。
 
 需要注意到的是，如果配置文件中没有进行server_name的配置，但是有域名解析到了服务器，这个时候Nginx会将该域名转发到配置中的第一个server上。
 
 类似情况也会有恶意解析域名到服务器的事情发生，比如有人知道你的服务器IP，将自己的域名直接解析到你的服务器，这样收割服务器的流量，解决方案比较也同样比较恶心，只需要在第一个server下的localtion中转发到一个静态页面提示就可以了。
 
 ```
 server {
       listen       80;
       server_name  *.com;
 
       location / {
            index   error.html;
       }
   }

 ```
 
> ### Nginx的location转发配置
 
 这里还要着重说明 `location` 参数，毕竟Nginx是一款反向代理服务器，看家本事都在location上面。
 
 ```
     Location区段匹配示例
     
     location = / {
     　　# 只匹配 / 的查询.
     　　[ configuration A ]
     }
     location / {
     　　# 匹配任何以 / 开始的查询，但是正则表达式与一些较长的字符串将被首先匹配。
     　　[ configuration B ]
     }
     location ^~ /images/ {
     　　# 匹配任何以 /images/ 开始的查询并且停止搜索，不检查正则表达式。
     　　[ configuration C ]
     }
     location ~* \.(gif|jpg|jpeg)$ {
     　　# 匹配任何以gif, jpg, or jpeg结尾的文件，但是所有 /images/ 目录的请求将在Configuration C中处
     　　理。
     　　[ configuration D ]
     } 
     
     各请求的处理如下例：
     ■/ → configuration A
     ■/documents/document.html → configuration B
     ■/images/1.gif → configuration C
     ■/documents/1.jpg → configuration D
 
 ```
 
 切记如果配置第三种转发一定要带 **/** 结尾，毕竟是吃过亏的人。
 
> ### 遇到过的问题
 
 如果有服务请求转发超过60s的，需要自己在location下增加 `proxy_send_timeout` 的时间。
 
 如果是客户端请求到Nginx的响应时间过长，则需要设置http下的 `client_body_timeout` 的时间。
 
 如果是服务端Nginx返回给客户端响应时间过长，则要设置 `send_timeout` 的时间。
 
---

---
## 总结
 
 Nginx开源后淘宝将Nginx进行了优化，项目名称[Tengine](http://tengine.taobao.org)，总而言之，Nginx真的是一款很棒的服务器。
 
> 链接
 
 🔗 [Nginx日志常用统计分析命令](https://blog.yanzhenyidai.com/2020/08/12/Nginx%E6%97%A5%E5%BF%97%E5%B8%B8%E7%94%A8%E7%BB%9F%E8%AE%A1%E5%88%86%E6%9E%90%E5%91%BD%E4%BB%A4/)

---