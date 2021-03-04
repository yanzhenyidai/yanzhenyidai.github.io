package com.yanzhenyidai.wiki.example.autotask;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author frank
 * @version 1.0
 * @date 2020-08-17 15:28
 */
public class Task {

    public static String[] persons = {"A", "B", "C", "D"};

    public static String[] tasks = {"0", "1", "2", "3", "4", "5", "6"};

    public static class Person {

        private String person;
        private List<String> task = new ArrayList<>();

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public List<String> getTask() {
            return task;
        }

        public void setTask(List<String> task) {
            this.task = task;
        }
    }

    public static void main(String[] args) {

        List<Person> personList = new ArrayList<>();

        int dve = tasks.length % persons.length;

        for (int i = 0; i < persons.length; i++) {
            Person person = new Person();
            person.setPerson(persons[i]);
            person.getTask().add(tasks[i]);

            personList.add(person);
        }

        for (int i = tasks.length - dve; i < tasks.length; i++) {
            Person person = new Person();

            Random random = new Random();

            person.setPerson(persons[random.nextInt(4)]);
            person.getTask().add(tasks[i]);

            personList.add(person);
        }

        System.out.println(JSON.toJSONString(personList));
    }
}
