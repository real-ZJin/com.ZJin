package com.ZJin.toMysqlFromHbase.mr;

import com.ZJin.toMysqlFromHbase.dao.MyDBWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CTReducer extends Reducer<Text,Text,MyDBWritable,NullWritable>{
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String[] arr = key.toString().split("[-]");
        int num = 0;
        int time = 0;
        for(Text t:values){
            num++;
            time += Integer.parseInt(t.toString());
        }
        MyDBWritable mw = new MyDBWritable(arr[0],arr[1],Integer.parseInt(arr[2]),num,time);
        context.write(mw,NullWritable.get());
    }
}
