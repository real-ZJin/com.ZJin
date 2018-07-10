package com.ZJin.consumer.test;

import com.ZJin.consumer.utils.PropertiesUtil;

public class PropertiesTest {
    public static void main(String[] args) {
        String property = PropertiesUtil.getProperty("kafka.topics");
        System.out.println(property);
    }
}
