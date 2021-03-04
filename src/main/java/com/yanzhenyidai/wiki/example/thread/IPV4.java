package com.yanzhenyidai.wiki.example.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author frank
 * @version 1.0
 * @date 2020-08-10 16:52
 */
public class IPV4 {

    public static void main(String[] args) throws IOException {


//        File file = new File("C:\\Users\\Admin\\Desktop\\access.log");
//
//        BufferedReader reader = new BufferedReader(new FileReader(file));

//        Pattern p = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
//        String regEx="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
//        String regEx="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        String regEx="\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
        String string = "172.69.33.177 - - [10/Aug/2020:15:23:34 +0800] \"GET /lib/font-awesome/fonts/fontawesome-webfont.woff2?v=4.7.0 HTTP/1.1\" 200 77252 \"https://blog.yanzhenyidai.com/lib/font-awesome/css/font-awesome.min.css?v=4.6.2\" \"Mozilla/5.0 (Linux; U; Android 8.1.0; zh-CN; EML-AL00 Build/HUAWEIEML-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 baidu.sogo.uc.UCBrowser/11.9.4.974 UWS/2.13.1.48 Mobile Safari/537.36 AliApp(DingTalk/4.5.11) com.alibaba.android.rimet/10487439 Channel/227200 language/zh-CN\"";
        Pattern p = Pattern.compile(regEx);
//        Pattern p = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");

        Matcher matcher = p.matcher(string);

        System.out.println(matcher.group());

//        reader.readLine();


//        int i = 0;
//        while (reader.readLine() != null) {
//            System.out.println(reader.readLine());
//            i++;
//        }
//
//        System.out.println(i);
//        reader.close();
    }

}
