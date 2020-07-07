
# 开发自己的微信小程序

> 由于近期在接触到了微信小程序，在熟悉了小程序的开发流程后，也是开发了一个自己的线上小程序，相当于一个 `个人发票夹` 的APP。

![miniprogram-logo.jpg](https://i.loli.net/2020/07/05/ZlbkXE2q816waOI.jpg)

---

## 开发工具

 在准备开发小程序之前，你需要有一个小程序的账号，并且拿到小程序的 appId，如果没有，可以到微信公众平台注册。[微信公众平台](https://mp.weixin.qq.com/)

> 微信小程序的开发模式接触下来的有下面两种，Wepy & 官方的开发者工具，两者都需要基于 `Node.js`

### Wepy

 Wepy刚开始是作为一款个人开发的框架出现，后面由于使用人数增多，现在已经被收录到微信开发工具当中。
 
 如果你熟练使用过 `Vue.js`，那么完全可以使用 Wepy 进行开发，过程很简单，只需要 `npm install @wepy/cli -g` 就可以开始后续开发操作。
 
 Wepy地址： [Wepy](https://wepyjs.github.io/wepy-docs/)。
 
 建议使用 `Wepy 2.X` 版本进行开发， `1.X` 在目前看来组件化是有点Old。
 
### 微信开发者工具

 微信开发者工具是官方推出的开发工具，包含调试、编辑、模拟三个必备的功能。
 
 ![wechat-develop.png](https://i.loli.net/2020/07/07/kCTrmWV2ESsulL7.png)
 
 优点是，官方出品，万无一失。
 缺点是，不知道咋回事，写着写着电脑CPU就莫名其妙的100%了。
 
 不过对于不熟悉 `Vue.js` 的同学来说，官方开发者工具开发流程真的很容易上手。
 
 微信开发者工具下载地址：[MiniProgram-DevelopTool](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)，强烈建议下载 `稳定版本`。

---

## 实现小程序

 以微信开发者工具为例，在工具中创建好项目后，下面是我自己的项目结构。
 
> app.json
 
```json
{
  "pages": [
    "pages/index_v2/index_v2",
    "pages/invoice_tab/invoice_tab",
    "pages/company/company",
    "pages/index/index",
    "pages/ap_invoice/ap_invoice",
    "pages/hi/hi",
    "pages/scan/scan",
    "pages/report/report",
    "pages/logs/logs",
    "pages/form/form",
    "pages/ap/ap",
    "pages/detail/detail",
    "pages/email/email"
  ],
  "window": {
    "backgroundTextStyle": "light",
    "navigationBarBackgroundColor": "#fff",
    "navigationBarTitleText": "WeChat",
    "navigationBarTextStyle": "black"
  },
  "style": "v2",
  "sitemapLocation": "sitemap.json",
  "tabBar": {
    "list": [
      {
        "pagePath": "pages/index_v2/index_v2",
        "text": "首页",
        "iconPath": "/pages/images/index/manage.png",
        "selectedIconPath": "/pages/images/index/manage.png"
      },
      {
        "pagePath": "pages/ap_invoice/ap_invoice",
        "text": "发票",
        "iconPath": "/pages/images/index/invoice.png",
        "selectedIconPath": "/pages/images/index/invoice.png"
      },
      {
        "pagePath": "pages/hi/hi",
        "text": "我的",
        "iconPath": "/pages/images/index/mine.png",
        "selectedIconPath": "/pages/images/index/mine.png"
      }
    ]
  }
}
```
 
 微信小程序中的页面路径都是 `pages/文件夹/page的名称`。

 - pages ：pages中包含的是所有的项目中的页面路径，你可以随意调整页面路径顺序，不过首页一定是出现在第一个位置的页面。
 - tabBar ： tabBar是左边模拟器中的下方三个选项卡，可以在tabBar-list中配置。
 
> pages

 pages下出现的一般为页面信息，当然也可以包括一些静态图片。
 
 在pages下新建页面的时候，可以先建一个页面的文件夹，然后再将页面新建到该文件夹中。
 
> 组件-components

 引入一些常用组件信息，比如 `WeUI`，`Echats`。
 
 组件信息需要到这些组件的官方项目中复制，然后粘贴到本地项目中。
 
 引入组件需要在新建的page下的 .json 文件中配置，如下是引入Echats：
 
```json
{
  "navigationBarTitleText": "图表",
  "usingComponents": {
    "ec-canvas": "../../plugins/ec-canvas/ec-canvas"
  }
}
```

---

## 线上环境

 小程序部署需要已经备案的域名，以及域名需要配置Https证书，如果没有Https证书，那么你可以参考[Cloudflare](../network/cloudflare.md)来进行免费配置。

 线上环境部署分为两部分。
 
> 后端代码部署

 当然如果小程序没有后端那么就没这个事了，后端部署的逻辑和普通的项目没有什么区别，服务器部署，并且开放请求端口。
 
> 小程序部署

 小程序本地测试通过后，编译一份最新的代码，可以点击微信开发者工具右上角上传按钮进行上传，上传的时候需要填写本次版本更新描述和版本号。
 
 上传完成后，需要登陆微信公众平台，在小程序的版本这一个页签中，手工点击进入审核流程，审核通过后，就马上可以发布小程序了。

---

## 帮助

 如果开发中有任何疑问，可以登入小程序开发社区进行查看，如果你没有任何UI，也可以使用官方的WeUI来进行开发。
 
 - [微信小程序开发社区](https://developers.weixin.qq.com/community/develop/question)
 - [微信小程序开发文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)
 - [WEUI](https://github.com/wechat-miniprogram/weui-miniprogram)
 - [Echats](https://github.com/wechat-miniprogram/weui-miniprogram)
 
 - 持续补充中

---

## 总结

 小程序的出现，使得一些开发者不必担心 IOS 和 Android 两个终端的APP开发，不过这样下来流量全部被微信给收割，有得必有失吧。
 
 参考资料：
 
 > 📕 [微信官方文档](https://developers.weixin.qq.com/doc/)

 
