package com.ZJin.hbase;

import com.ZJin.mysql.MysqlUitls;
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

public class HbaseFactory {
    /*
        发布微博内容
     */
    public static void sendWeibo(String uid,String msg) throws Exception{
        //a、微博内容表(Content)中添加1条数据
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table content = conn.getTable(TableName.valueOf("weibo:Content"));
        //设置rowkey
        long sendTime = System.currentTimeMillis();
        String rowkey_C = uid+"-"+sendTime;
        Put put_C = new Put(rowkey_C.getBytes());
        //这里我不清楚是msg.getBytes()好还是Bytes.toBytes(msg)好，还是都行
        put_C.addColumn("info".getBytes(),"content".getBytes(),msg.getBytes());
        content.put(put_C);

        //b、微博收件箱表(RCEmail)对所有粉丝用户添加数据
        //1.先获取uid粉丝的id
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        ResultScanner scanner = relations.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while(iterator.hasNext()){
            Result next = iterator.next();
            //获取粉丝id
            for (Cell cell : next.rawCells()) {
                //获取粉丝的row
                if ("fans".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                    if (Bytes.toString(CellUtil.cloneRow(cell)).contains("0003")) {
                        byte[] fans_id = CellUtil.cloneValue(cell);
                        //2.向微博邮箱添加数据，此时rowkey=fans_id,value=关注用户发动态的rowkey_C
                        Put put_E = new Put(fans_id);
                        String colName = Bytes.toString(fans_id)+"-"+sendTime;
                        put_E.addColumn(Bytes.toBytes("info"),Bytes.toBytes(colName),Bytes.toBytes(rowkey_C));
                        email.put(put_E);
                    }
                }
            }
        }
    }
    /*
        添加关注用户
     */
    public static void addAttend(String uid,String att_id) throws Exception{
        // a、在微博用户关系表中，对当前主动操作的用户添加新关注的好友
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        long times = System.currentTimeMillis();
        String rowkey_A = uid+"-"+times;
        Put put_att = new Put(Bytes.toBytes(rowkey_A));
        put_att.addColumn(Bytes.toBytes("attends"),Bytes.toBytes("attends_id"),Bytes.toBytes(att_id));
        // b、在微博用户关系表中，对被关注的用户添加新的粉丝
        String rowkey_F = att_id+"-"+times;
        Put put_fans = new Put(Bytes.toBytes(rowkey_F));
        put_fans.addColumn(Bytes.toBytes("fans"),Bytes.toBytes("fans_id"),Bytes.toBytes(uid));
        //创建
        relations.put(put_att);
        relations.put(put_fans);
        // c、微博收件箱表中添加所关注的用户发布的微博
        Table content = conn.getTable(TableName.valueOf("weibo:Content"));
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        Put put_E = new Put(Bytes.toBytes(uid));
        ResultScanner scanner = content.getScanner(new Scan());
        Iterator<Result> iterator = scanner.iterator();
        while(iterator.hasNext()){
            Result next = iterator.next();
            for(Cell cell:next.rawCells()){
                if(Bytes.toString(CellUtil.cloneRow(cell)).contains(att_id)){
                    byte[] rowkey_C = CellUtil.cloneRow(cell);
                    //获取用户发动态的时间
                    String time_C = Bytes.toString(rowkey_C).split("-")[1];
                    String colName = uid+"-"+time_C;
//                  put_E.addColumn(Bytes.toBytes("info"),Bytes.toBytes(colName),rowkey_C);
                    put_E.addColumn(Bytes.toBytes("info"),Bytes.toBytes(colName),System.currentTimeMillis(),rowkey_C);
                    email.put(put_E);
                }
            }
        }
    }
    /*
        移除（取关）用户
     */
    public static void deleteAttend(String uid,String att_id) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table relations = conn.getTable(TableName.valueOf("weibo:Relations"));
        ResultScanner scanner_R = relations.getScanner(new Scan());
        Iterator<Result> iterator_R = scanner_R.iterator();
        while(iterator_R.hasNext()){
            Result next = iterator_R.next();
            for(Cell cell : next.rawCells()){
                //获取粉丝的row
                if("attends".equals(Bytes.toString(CellUtil.cloneFamily(cell)))){
                    if(Bytes.toString(CellUtil.cloneRow(cell)).contains(uid)&&att_id.equals(Bytes.toString(CellUtil.cloneValue(cell)))){
                        byte[] row_F = CellUtil.cloneRow(cell);
                        //a、在微博用户关系表中，对被取关的用户移除粉丝
                        Delete delete_F = new Delete(row_F);
                        relations.delete(delete_F);
                    }
                    //获取被关注者的row
                }else if("fans".equals(Bytes.toString(CellUtil.cloneFamily(cell)))){
                    if(Bytes.toString(CellUtil.cloneRow(cell)).contains(att_id)&&uid.equals(Bytes.toString(CellUtil.cloneValue(cell)))){
                        byte[] row_A = CellUtil.cloneRow(cell);
                        //b、在微博用户关系表中，对当前主动操作的用户移除取关的好友(attends)
                        Delete delete_A = new Delete(row_A);
                        relations.delete(delete_A);
                    }
                }
            }
        }
        //c、微博收件箱中删除取关的用户发布的微博
        //0001   column=info:0001-1528444081660, timestamp=1528472881659, value=0003-1528444081660
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        ResultScanner scanner_E = email.getScanner(new Scan());
        Iterator<Result> iterator_E = scanner_E.iterator();
        while (iterator_E.hasNext()){
            Result next = iterator_E.next();
            for(Cell cell:next.rawCells()){
                byte[] rowkey_E = CellUtil.cloneRow(cell);
                if(uid.equals(Bytes.toString(rowkey_E))){
                    if(Bytes.toString(CellUtil.cloneValue(cell)).contains(att_id)){
                        byte[] fluName = CellUtil.cloneQualifier(cell);
                        Delete delete = new Delete(rowkey_E);
                        delete.addColumn(Bytes.toBytes("info"),fluName);
                        email.delete(delete);
                    }
                }
            }
        }
    }
    /*
        获取关注的人的微博内容
        a、从微博收件箱中获取所关注的用户的微博RowKey
        b、根据获取的RowKey，得到微博内容
     */
    public static List<Content> getContent(String uid) throws Exception {
        List<Content> list = new ArrayList<>();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table email = conn.getTable(TableName.valueOf("weibo:RCEmail"));
        Table content = conn.getTable(TableName.valueOf("weibo:Content"));
        ResultScanner scanner_E = email.getScanner(new Scan());
        Iterator<Result> iterator_E = scanner_E.iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        while (iterator_E.hasNext()) {
            Result next = iterator_E.next();
            for (Cell cell : next.rawCells()) {
                if (uid.equals(Bytes.toString(CellUtil.cloneRow(cell)))) {
                    Content cont = new Content();
                    //email表的value，也就是content的rowkey
                    byte[] rowkey_C = CellUtil.cloneValue(cell);
                    //获取发布动态人的信息
                    ResultScanner scanner = content.getScanner(new Scan());
                    Iterator<Result> iterator = scanner.iterator();
                    while(iterator.hasNext()){
                        Result next1 = iterator.next();
                        for(Cell cell1:next1.rawCells()){
                            if (Bytes.toString(CellUtil.cloneRow(cell1)).contains(Bytes.toString(rowkey_C))) {
                                Content contt = new Content();
                                String contentt = Bytes.toString(CellUtil.cloneValue(cell1));
                                String row = Bytes.toString(CellUtil.cloneRow(cell1));
                                String id = row.split("-")[0];
                                String time = row.split("-")[1];
                                String format = sdf.format(Long.parseLong(time));
                                contt.setDate(format);
                                contt.setId(id);
                                contt.setUsername(MysqlUitls.getNameByid(id));
                                contt.setContent(contentt);
                                list.add(contt);
                            }
                        }
                    }
                }
            }
        }
        //展示自己的动态
        ResultScanner uidscan = content.getScanner(new Scan());
        Iterator<Result> iterator = uidscan.iterator();
        while(iterator.hasNext()){
            Result uidNext = iterator.next();
            for(Cell uidCell:uidNext.rawCells()){
                if (Bytes.toString(CellUtil.cloneRow(uidCell)).contains(uid)) {
                    Content contt = new Content();
                    String contentt = Bytes.toString(CellUtil.cloneValue(uidCell));
                    String row = Bytes.toString(CellUtil.cloneRow(uidCell));
                    String id = row.split("-")[0];
                    String time = row.split("-")[1];
                    String format = sdf.format(Long.parseLong(time));
                    contt.setDate(format);
                    contt.setId(id);
                    contt.setUsername(MysqlUitls.getNameByid(id));
                    contt.setContent(contentt);
                    list.add(contt);
                }
            }
        }
        return list;
    }
}
