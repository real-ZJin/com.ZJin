package com.ZJin.consumer.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties props = null;

    static {
        props = new Properties();
        try {
            props.load(ClassLoader.getSystemResourceAsStream("hbase_consumer.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProperty(String key){
        return props.getProperty(key);
    }
}
