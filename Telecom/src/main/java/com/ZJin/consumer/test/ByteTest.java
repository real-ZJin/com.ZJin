package com.ZJin.consumer.test;

import com.ZJin.consumer.utils.PropertiesUtil;
import org.apache.hadoop.hbase.util.Bytes;

public class ByteTest {
    public static void main(String[] args) {
        byte[] by = Bytes.toBytes("00|");
        int i = 10;
        for(byte b:by){
            i++;
            Integer m =b^i;
            System.out.println(m+":"+m.hashCode());
            System.out.println(PropertiesUtil.getProperty("hbase.calllog.namespace"));
        }
    }
}
