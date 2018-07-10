package com.ZJin.consumer.kafka;

import com.ZJin.consumer.hbase.HbaseDAO;
import com.ZJin.consumer.utils.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;

public class HbaseConsumer {
    public static void main(String[] args) {
        KafkaConsumer<String,String> kc = new KafkaConsumer<String, String>(PropertiesUtil.props);
        kc.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));
        HbaseDAO hdao = new HbaseDAO();
        while (true){
            //获取数据，100ms还没获取将重试
            ConsumerRecords<String,String> records = kc.poll(100);
            for(ConsumerRecord record : records){
                String msg = record.value().toString();
                System.out.println(msg);
                hdao.put(msg);
            }
        }
    }
}
