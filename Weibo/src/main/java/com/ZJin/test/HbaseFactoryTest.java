package com.ZJin.test;

import com.ZJin.hbase.Content;
import com.ZJin.hbase.HbaseFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class HbaseFactoryTest {
    public static void main(String[] args) throws Exception{
        test10();
    }
    private static void test10() throws Exception {
        List<Content> contents = HbaseFactory.getContent("1399015897");
        for(Content content:contents){
            System.out.println(content.getContent());
        }
    }
    private static void test9() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        ResultScanner scanner_E = email.getScanner(new Scan());
        Iterator<Result> iterator_E = scanner_E.iterator();
        while (iterator_E.hasNext()){
            Result next = iterator_E.next();
            for(Cell cell:next.rawCells()){
                byte[] rowkey_E = CellUtil.cloneRow(cell);
                if("1157439376".equals(Bytes.toString(rowkey_E))){
                    if(Bytes.toString(CellUtil.cloneValue(cell)).contains("0003")){
                        byte[] fluName = CellUtil.cloneQualifier(cell);
                        Delete delete = new Delete(rowkey_E);
                        delete.addColumn(Bytes.toBytes("info"),fluName);
                        email.delete(delete);
                    }
                }
            }
        }
    }
    private static void test8() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String date = sdf.format(Long.parseLong("1528443056343"));
        System.out.println(date);
    }
    private static void test7() throws Exception {
        List<Content> msgs = HbaseFactory.getContent("0001");
        for(Content msg : msgs){
            System.out.println(msg.getId()+"==="+msg.getContent()+"==="+msg.getDate());
        }
    }
    private static void test6() throws Exception {
        HbaseFactory.deleteAttend("1399015897","1157439376");
    }
    private static void test5() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        ResultScanner scanner_E = email.getScanner(new Scan());
        Iterator<Result> iterator_E = scanner_E.iterator();
        while (iterator_E.hasNext()){
            Result next = iterator_E.next();
            for(Cell cell:next.rawCells()){
                //0001-1528444081660
                if("0001".equals(Bytes.toString(CellUtil.cloneRow(cell)))){
                    if(Bytes.toString(CellUtil.cloneValue(cell)).contains("0003")){
                        System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
                    }
                }
            }
        }
    }
    private static void test4() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        ResultScanner scanner = relations.getScanner(new Scan());
        System.out.println(scanner);
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()) {
            Result next = iterator.next();
            //获取粉丝id
//            byte[] fans_id = next.getValue("fans".getBytes(), "fans_id".getBytes());
//            System.out.println("的粉丝是：" + Bytes.toString(fans_id));
//            if(fans_id==null)break;
            for (Cell cell : next.rawCells()) {
                //获取粉丝的row
                if ("fans".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                    if (Bytes.toString(CellUtil.cloneRow(cell)).contains("0003")) {
                        System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
                    }
                    //获取被关注者的row
                }
            }
        }
    }
    private static void test3() throws Exception{
            HbaseFactory.deleteAttend("1157439376", "0002");
    }
    private static void test2() throws Exception{
        HbaseFactory.addAttend("1399015897","1157439376");
//        HbaseFactory.sendWeibo("0003","端午不回家了");
//        HbaseFactory.addAttend("0004","0002");
    }
    private static void test1() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("weibo:Content"));
        ResultScanner scanner = table.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result next = iterator.next();
            Cell[] cells = next.rawCells();
            for(Cell cell:cells){
                System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
    }
}
