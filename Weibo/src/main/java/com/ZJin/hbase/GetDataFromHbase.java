package com.ZJin.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetDataFromHbase {
    public static List<String> getAtt(String uid) throws Exception {
        List<String> list = new ArrayList<>();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        ResultScanner scanner = relations.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result next = iterator.next();
            for(Cell cell:next.rawCells()){
                if("attends".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                    if (Bytes.toString(CellUtil.cloneRow(cell)).contains(uid)) {
                        String att = Bytes.toString(CellUtil.cloneValue(cell));
                        list.add(att);
                    }
                }
            }
        }
        return list;
    }
    public static List<String> getfans(String uid) throws Exception {
        List<String> list = new ArrayList<>();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        ResultScanner scanner = relations.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result next = iterator.next();
            for(Cell cell:next.rawCells()){
                if("fans".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                    if (Bytes.toString(CellUtil.cloneRow(cell)).contains(uid)) {
                        String fans = Bytes.toString(CellUtil.cloneValue(cell));
                        list.add(fans);
                    }
                }
            }
        }
        return list;
    }
    public static List<Content> getContentById(String uid) throws Exception {
        List<Content> list = new ArrayList<>();
        Configuration conf = HBaseConfiguration.create();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Content"));
        ResultScanner scanner = relations.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result next = iterator.next();
            for(Cell cell:next.rawCells()){
                if (Bytes.toString(CellUtil.cloneRow(cell)).contains(uid)) {
                    Content cont = new Content();
                    String content = Bytes.toString(CellUtil.cloneValue(cell));
                    String row = Bytes.toString(CellUtil.cloneRow(cell));
                    String user = row.split("-")[0];
                    String time = row.split("-")[1];
                    String format = sdf.format(Long.parseLong(time));
                    cont.setDate(format);
                    cont.setId(user);
                    cont.setContent(content);
                    list.add(cont);
                }
            }
        }
        return list;
    }
}
