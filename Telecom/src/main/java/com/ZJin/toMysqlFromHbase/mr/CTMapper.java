package com.ZJin.toMysqlFromHbase.mr;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class CTMapper extends TableMapper<Text,Text>{
    int i = 0;
    @Override
    /*
        row:05_19721236599_20171201195834_14464715497_1_0824
        主表：
            id：++i
            number：substring(3,14)
            date：substring(15,23)
            flag：substring(42,43)
            time：substring(44)
        附表1：
            number：
            name：
     */
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        String rowKey = Bytes.toString(key.get());
        int id = ++i;
        String number = rowKey.substring(3,14);
        String date = rowKey.substring(15,21);
        String flag = rowKey.substring(42,43);
        String time = rowKey.substring(44);
        String msg = number+"-"+date+"-"+flag;
        context.write(new Text(msg),new Text(time));
    }
}
