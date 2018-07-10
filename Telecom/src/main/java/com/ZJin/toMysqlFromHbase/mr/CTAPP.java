package com.ZJin.toMysqlFromHbase.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;


public class CTAPP {
    public static void main(String[] args) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Job job = Job.getInstance(conf);
        job.setJarByClass(CTAPP.class);

        initDBWriter(job);
        initMapper(job);
        initReducer(job);

        job.waitForCompletion(true);
    }

    private static void initDBWriter(Job job) throws Exception{
        //配置数据库信息
        String driverclass = "com.mysql.jdbc.Driver" ;
        String url = "jdbc:mysql://localhost:3306/mydb2018" ;
        String username= "root" ;
        String password = "root" ;
        //设置数据库配置
        DBConfiguration.configureDB(job.getConfiguration(),driverclass,url,username,password);
        DBOutputFormat.setOutput(job,"tb_call","number","call_date","flag","call_time","call_num");
    }

    private static void initReducer(Job job) {
        job.setReducerClass(CTReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
    }

    private static void initMapper(Job job) throws Exception{
        TableMapReduceUtil.initTableMapperJob(
                "ns_ct:calllog",
                new Scan(),
                CTMapper.class,
                Text.class,
                Text.class,
                job,
                false);
    }

}