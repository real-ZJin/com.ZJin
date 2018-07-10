package com.ZJin.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class InitHbase {
    //检查名称空间是否创建，没创建的话就创建一个
    public static void createNamespace() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        NamespaceDescriptor[] nds = admin.listNamespaceDescriptors();
        boolean flag = true;
        for(NamespaceDescriptor nd:nds){
            String ndName = nd.getName();
            if("weibo".equals(ndName)){
                flag=false;
            }
        }
        if(flag){
            NamespaceDescriptor weibo = NamespaceDescriptor.create("weibo").build();
            admin.createNamespace(weibo);
        }
    }
   /*
        检查微博内容表是否创建，没创建的话就创建一个
        内容表数据结构
            列族：info
            rowkey：发布微博用户id+时间
            列：content   value：发布的内容
    */
    public static void CreateContent() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection();
        Admin admin = conn.getAdmin();
        TableName[] tableNames = admin.listTableNames();
        boolean flag = true;
        for(TableName tn : tableNames){
            String name = tn.getNameAsString();
            String[] arr = name.split(":");
            if("weibo".equals(arr[0])){
                if("Content".equals(arr[1])){
                    flag=false;
                }
            }
        }
        if(flag){
            HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("weibo:Content"));
            HColumnDescriptor info = new HColumnDescriptor("info");

            //设置同一个rowkey缓存的数量
            info.setMinVersions(1);
            info.setMaxVersions(1);

            htd.addFamily(info);
            admin.createTable(htd);
        }
    }
    /*
        检查用户关系表是否创建，没创建的话就创建一个
        用户关系表数据结构
            列族：attends
            rowkey：A关注B,则rk=A_id+time
            列：attends_id   value：B_id

            列族：fans
            rowkey：A成为B的粉丝,则rk=B_id+time
            列：fans_id   value：A_id
     */
    public static void createRelations() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        TableName[] tableNames = admin.listTableNames();
        boolean flag = true;
        for(TableName tn:tableNames){
            String name = tn.getNameAsString();
            String[] arr = name.split(":");
            if("weibo".equals(arr[0])){
                if("Relations".equals(arr[1])){
                    flag=false;
                }
            }
        }
        if(flag){
            HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("weibo:Relations"));
            HColumnDescriptor attends = new HColumnDescriptor("attends");
            HColumnDescriptor fans = new HColumnDescriptor("fans");

            attends.setMaxVersions(1);
            attends.setMinVersions(1);
            fans.setMaxVersions(1);
            fans.setMinVersions(1);

            htd.addFamily(attends).addFamily(fans);
            admin.createTable(htd);
        }
    }
    /*
        检查微博邮箱是否创建，没创建的话就创建一个
        微博邮箱表数据结构
            列族：info
            rowkey：粉丝id
            列：粉丝id+关注人发动态的时间   value：发布微博用户id+时间，也就是content表的rowkey，
                                              后面可以根据这个rowkey查看对应的value，也就是动态内容
     */
    public static void createRCEmail() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        TableName[] tableNames = admin.listTableNames();
        boolean flag = true;
        for(TableName tn : tableNames){
            String name = tn.getNameAsString();
            String[] arr = name.split(":");
            if("weibo".equals(arr[0])){
                if("RCEmail".equals(arr[1])){
                    flag=false;
                }
            }
        }
        if(flag){
            HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("weibo:RCEmail"));
            HColumnDescriptor info = new HColumnDescriptor("info");

            info.setMinVersions(1);
            info.setMaxVersions(1000);

            htd.addFamily(info);
            admin.createTable(htd);
        }
    }
}
