package com.ZJin.consumer.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;

public class HbaseUtil {
    //初始化命名空间
    public static void initNs(Configuration conf,String ns_name) throws Exception {
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        NamespaceDescriptor nd = NamespaceDescriptor.create(ns_name).build();
        admin.createNamespace(nd);
        conn.close();
        admin.close();
    }
    //判断表是否存在
    public static boolean isExistTable(Configuration conf,String tb_name) throws Exception {
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        boolean isExist = admin.tableExists(TableName.valueOf(tb_name));
        conn.close();
        admin.close();
        return isExist;
    }
    //创建表
    public static void createTable(Configuration conf,String tb_name,int regions,String... colunFamily)throws Exception{
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //如果存在该表，就返回
        if(isExistTable(conf,tb_name))return;
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tb_name));
        for(String col : colunFamily){
            //创建表时必须指定列族
            htd.addFamily(new HColumnDescriptor(col));
        }
        //创建带分区的表：分区规则{([-oo,[48, 48, 124]),([[48, 48, 124],[48, 49, 124])...}
        //也就是{([-oo,00|]),([00|,01|])...};主要用途解决负载均衡
        admin.createTable(htd,getSplitKeys(regions));
        admin.close();
        conn.close();
    }
    public static byte[][] getSplitKeys(int regions){
        //定义一个存放分区键的数组
        String[] keys = new String[regions];
        //目前推算，region个数不会超过2位数，所以region分区键格式化为两位数字所代表的字符串
        DecimalFormat df = new DecimalFormat("00");
        for(int i = 0; i < regions; i ++){
            keys[i] = df.format(i) + "|";
        }
        //第一个[]存储region，第二个[]存储"00|","01|"...也即是第一层数组长度为regions，第二层数组长度为3
        byte[][] splitKeys = new byte[regions][];
        //生成byte[][]类型的分区键的时候，一定要保证分区键是有序的
        TreeSet<byte[]> treeSet = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for(int i = 0; i < regions; i++){
            treeSet.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> splitKeysIterator = treeSet.iterator();
        int index = 0;
        while(splitKeysIterator.hasNext()){
            byte[] b = splitKeysIterator.next();
            //将[48, 48, 124]赋值给splitKeys[0],[48, 49, 124]赋值给splitKeys[1]....
            splitKeys[index ++] = b;
        }
        //00|变为byte[]数组后对应的值为[48, 48, 124]
        return splitKeys;
    }
    //rowkey格式：regionCode_call1_buildTime_call2_flag_duration
    public static String getRowKey(String regionCode, String call1, String buildTime, String call2, String flag, String duration){
        StringBuilder sb = new StringBuilder();
        sb.append(regionCode + "_")
                .append(call1 + "_")
                .append(buildTime + "_")
                .append(call2 + "_")
                .append(flag + "_")
                .append(duration);
        return sb.toString();
    }
    //获得分区号；通话建立时间：2017-01-10 11:20:30 -> 20170110112030
    public static String getRegionCode(String call1, String buildTime, int regions){
        int len = call1.length();
        //取出后4位号码
        String lastPhone = call1.substring(len - 4);
        //取出年月；去除"-" ":" " "
        String ym = buildTime
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .substring(0, 6);
        //离散操作1
        Integer x = Integer.valueOf(lastPhone) ^ Integer.valueOf(ym);
        //离散操作2:x难道不是等于y？10.hashCode()=10
        int y = x.hashCode();
        //生成分区号
        int regionCode = y % regions;
        //格式化分区号
        DecimalFormat df = new DecimalFormat("00");
        return  df.format(regionCode);
    }
}
