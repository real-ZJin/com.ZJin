package com.ZJin.consumer.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;

public class ScanTest {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("mydb:tb1"));
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("0002"));
        scan.setStopRow(Bytes.toBytes("0005"));
        ResultScanner scanner = table.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();
        while(iterator.hasNext()){
            Result next = iterator.next();
            for(Cell cell:next.rawCells()){
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell)));
            }
        }
    }
}
