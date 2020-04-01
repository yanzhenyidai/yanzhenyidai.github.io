
# Cloudflare

> Cloudflare是美国的一家网络性能和安全公司，近期由于自己域名HTTP证书到期，了解到了[Cloudflare](https://www.cloudflare.com)，用到了它提供的CDN以及SSL

---
## 如何设置CDN

 登入[Cloudflare](https://www.cloudflare.com)网站，点击Add Site按钮，输入自己的网站域名，出现下面界面时
 
 <img src="https://i.loli.net/2020/03/25/hWlxot9pu8wnPGc.png" width="60%"/>

 选择免费Free，当然土豪随意，然后Cloudflare会匹配出当前域名和域名的二级域名，Confirm后，会出现一个DNS的地址。
 
 这个时候需要到域名注册商，比如阿里云，百度云下将域名的DNS更换成Cloudflare提供的DNS地址。
 
 设置完成后，等待大概60-120min后，DNS缓存应该清除了，此时ping自己的域名，对应的IP就是Cloudflare的IP了。
 
---

---
## 如何配置SSL
 
 设置好了DNS后，可以进入Cloudflare Dashboard，选择SSL/TLS

 <img src="https://i.loli.net/2020/03/25/rXS4mKOuwNM1UZs.png" width="60%"/>

 可以按照上面设置，选择Full(strict)，双向验证。
 
 再选择上方的Origin Server - Create Certificate，按照默认选择RSA的Private Key类型，点继续后，出现了生成的PEM和KEY。
 
 将PEM信息复制，本地粘贴创建.pem文件，KEY信息同样复制，粘贴创建.key文件，证书生成后，可以自己导到服务器进行配置。
 
---

--- 
## Cloudflare使用感受

 由于Cloudflare是国外的网络服务商，对国内的服务器CDN没有太大效果，不过SSL是真心赞，对于想自己建站但是HTTPS证书太贵的情况下。
 
 况且使用Cloudflare后，直接ping域名网站的IP出来的是Cloudflare的IP，会对一些想暴力破解的操作有一定阻止。
 
 Dashboard界面确实不错，能看出访问网站的流量，这个在阿里云上还要买域名服务才能看到。
 
---