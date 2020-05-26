package com.yanzhenyidai.wiki.example.ocr;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.IOException;
import java.util.UUID;

/**
 * @author frank
 * @version 1.0
 * @date 2020-04-14 14:40
 */
public class RuiQiTest {

    public static void main(String... args) throws IOException {
//
//        String appKey = "5e956c6e"; //这里输入提供的app_key
//        String appSecret = "85bca878975a4cef2ea04cd4c866f71a"; //这里输入提供的app_secret
//
////        String imageUrl = "http://fapiao.glority.cn/dist/img/sample.jpg";
////        String imageUrl = "http://snyimg2.vaiwan.com/image/autohome_image/gdwssb.jpg";
//        String imageUrl = "http://snyimg2.vaiwan.com/image/autohome_image/9bd31fd18efed5e4d9727486703a02e.jpg";
////        String host = "http://fapiao.glority.cn/v1/item/get_item_info";
//        String host = "https://fapiao.glority.cn/v1/item/get_multiple_items_info";
//        long timestamp = System.currentTimeMillis() / 1000;
//        String token = new Md5Hash(appKey + "+" + timestamp + "+" + appSecret).toString();
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(host)
//                .post(new FormBody.Builder()
//                        .add("app_key", appKey)
//                        .add("timestamp", String.valueOf(timestamp))
//                        .add("token", token)
//                        .add("image_url", imageUrl)
//                        .build())
//                .build();
//
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
        System.out.println(UUID.randomUUID().toString());
    }

}
