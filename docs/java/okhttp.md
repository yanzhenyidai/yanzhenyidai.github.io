
# 一次使用OKHTTP的心痛历程

> 最近由于一些不得已的原因，接触到了OKHttp，说起来也挺Dan疼的，之前同事将生产附件上传地址配置成了测试地址，还好数量不多，没有造成太大的影响，况且的是这位同事又离职了，当时只能在心中
默默的问候了他N遍，当然问候完了之后，也不得不继续数据同步的工作。😭

> OKHTTP官方地址：[okHttp](https://square.github.io/okhttp/)

---

## 介绍

 由于OkHttp官网的介绍十分详细，这里只能贴上一段翻译过后的introduce：
 
 > HTTP是现代应用网络的一种方式。这就是我们交换数据和媒体的方式。高效地使用HTTP可以让你的东西更快地加载并节省带宽。
 
 OkHttp是一个高效的Http客户端，在默认的情况下：
 
 - HTTP / 2支持允许对同一主机的所有请求共享一个套接字。
 - 连接池可减少请求延迟（如果HTTP / 2不可用）。
 - 透明的GZIP缩小了下载大小。
 - 响应缓存可以完全避免网络重复请求。
 
 不过在我使用下来，OkHttp比 `apache-http` 好用太多，层次结构较直观。

--- 

## 使用场景

 本次场景是将上传到测试环境的文件信息，下载到本地，然后再上传到生产环境。
 
 解决过程如下：
 
 - 将错误数据从数据库表中粘贴到本地新建的一个Excel文件中。（毕竟直接连接数据库风险更大）
 - 读取Excel内的信息，获取文件地址。
 - 请求文件地址，获取到流文件信息。
 - 拿到流文件信息，拼接上传数据，上传到新的生产环境中。
 - 上传完成后，获取到生产环境文件地址。
 - 获取到生产文件地址的同时，生成更新的SQL语句。
 - 到数据库中执行SQL语句。
 
---

## 使用过程

 本次使用没有搭建新的工程，直接再 `src/test/java` 目录下新建一个Java类。
 
 引入OKHttp的依赖：
 
```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>3.3.1</version>
</dependency>
```

 在引入了 `okhttp` 的jar包后，基本上就可以开始随心所欲的进行自己任意丧心病狂的Http请求了。
 
 比如，它直接同步和异步的请求：
 
 > 同步GET 
 
```java
  private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    Request request = new Request.Builder()
        .url("https://publicobject.com/helloworld.txt")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      Headers responseHeaders = response.headers();
      for (int i = 0; i < responseHeaders.size(); i++) {
        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
      }

      System.out.println(response.body().string());
    }
  }
```

 > 异步GET
 
```java
 private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    Request request = new Request.Builder()
        .url("http://publicobject.com/helloworld.txt")
        .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
          if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

          Headers responseHeaders = response.headers();
          for (int i = 0, size = responseHeaders.size(); i < size; i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
          }

          System.out.println(responseBody.string());
        }
      }
    });
  }
```

 > Header信息
 
```java
private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    Request request = new Request.Builder()
        .url("https://api.github.com/repos/square/okhttp/issues")
        .header("User-Agent", "OkHttp Headers.java")
        .addHeader("Accept", "application/json; q=0.5")
        .addHeader("Accept", "application/vnd.github.v3+json")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println("Server: " + response.header("Server"));
      System.out.println("Date: " + response.header("Date"));
      System.out.println("Vary: " + response.headers("Vary"));
    }
  }
```

 > POST请求流信息
 
```java
public static final MediaType MEDIA_TYPE_MARKDOWN
      = MediaType.parse("text/x-markdown; charset=utf-8");

  private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    RequestBody requestBody = new RequestBody() {
      @Override public MediaType contentType() {
        return MEDIA_TYPE_MARKDOWN;
      }

      @Override public void writeTo(BufferedSink sink) throws IOException {
        sink.writeUtf8("Numbers\n");
        sink.writeUtf8("-------\n");
        for (int i = 2; i <= 997; i++) {
          sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
        }
      }

      private String factor(int n) {
        for (int i = 2; i < n; i++) {
          int x = n / i;
          if (x * i == n) return factor(x) + " × " + i;
        }
        return Integer.toString(n);
      }
    };

    Request request = new Request.Builder()
        .url("https://api.github.com/markdown/raw")
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println(response.body().string());
    }
  }
```

 > POST请求File信息
 
```java
  public static final MediaType MEDIA_TYPE_MARKDOWN
      = MediaType.parse("text/x-markdown; charset=utf-8");

  private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    File file = new File("README.md");

    Request request = new Request.Builder()
        .url("https://api.github.com/markdown/raw")
        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println(response.body().string());
    }
  }
```

 > Post表单提交
 
```java
 private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    RequestBody formBody = new FormBody.Builder()
        .add("search", "Jurassic Park")
        .build();
    Request request = new Request.Builder()
        .url("https://en.wikipedia.org/w/index.php")
        .post(formBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println(response.body().string());
    }
  }
```

 > POST多个Body请求
 
```java
  /**
   * The imgur client ID for OkHttp recipes. If you're using imgur for anything other than running
   * these examples, please request your own client ID! https://api.imgur.com/oauth2
   */
  private static final String IMGUR_CLIENT_ID = "...";
  private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

  private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("title", "Square Logo")
        .addFormDataPart("image", "logo-square.png",
            RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
        .build();

    Request request = new Request.Builder()
        .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
        .url("https://api.imgur.com/3/image")
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println(response.body().string());
    }
  }
```

 因为使用过程中大多数都是按照官网的例子来进行，所以这次使用的代码是类似于官方提供的例子，当然也是不太好意思贴出来，哈哈。😁 

---

## 总结

 OkHttp算得上是相见恨晚，之前一遍一遍写 `apache-http` 的时候就觉得 `apche` 有点冗余，就是想有一个轻量级的，比较好上手，容易懂的http-client，不过现在接触到了 okhttp，还是得感谢那位配错地址的兄弟。😂
 
 以上更多请求例子可以访问：[OKhttp-Request-example](https://square.github.io/okhttp/recipes/)
 
 参考资料：
 
 > [OkHttp.io](https://square.github.io/) <br>
 > [OKhttp-Request-example](https://square.github.io/okhttp/recipes/) <br>
 > [OKHttp-Github](https://github.com/square/okhttp/)

