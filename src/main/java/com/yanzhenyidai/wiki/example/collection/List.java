package com.yanzhenyidai.wiki.example.collection;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author frank
 * @version 1.0
 * @date 2020-03-26 17:01
 */
public class List {

    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        user.setName("Tan");

        User user1 = new User();
        user1.setId(0L);
        user.setName("Zhen");

        java.util.List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);

        Collections.sort(userList, Comparator.comparing(User::getId).reversed());

        System.out.println(JSON.toJSONString(userList.get(0)));
    }


}
