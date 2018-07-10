package com.ZJin.producer;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.LongStream;

//生产数据
public class ProductLog {
    //存储电话号码
    private List<String> phoneList = new ArrayList<>();
    //存储电话和姓名映射
    private Map<String, String> phoneMap = new HashMap<>();
    //统计开始时间
    String start = "2017-01-01 00:00:00";
    //统计结束时间
    String end = "2017-12-31 23:59:59";
    //主叫
    private String caller = null;
    //主叫名字
    private String callerName = null;
    //被叫
    private String callee = null;
    //被叫名字
    private String calleeName = null;
    //通话时间
    private String callTime = null;
    //通话时长
    private String duration = null;
    //日志格式
    private String log = null;
    public void init(){
        phoneList.add("18141966329");
        phoneList.add("18866047052");
        phoneList.add("13744475993");
        phoneList.add("19721236599");
        phoneList.add("15587389358");
        phoneList.add("15256895961");
        phoneList.add("16661565697");
        phoneList.add("13422993762");
        phoneList.add("18622382530");
        phoneList.add("17645008690");
        phoneList.add("15441997997");
        phoneList.add("17250803381");
        phoneList.add("14182855303");
        phoneList.add("18369520821");
        phoneList.add("17019156108");
        phoneList.add("16388355107");
        phoneList.add("14464715497");
        phoneList.add("13762892588");
        phoneList.add("17062526901");
        phoneList.add("18478232361");

        phoneMap.put("18141966329", "李雁");
        phoneMap.put("18866047052", "卫艺");
        phoneMap.put("13744475993", "仰莉");
        phoneMap.put("19721236599", "陶欣悦");
        phoneMap.put("15587389358", "施梅梅");
        phoneMap.put("15256895961", "金虹霖");
        phoneMap.put("16661565697", "魏明艳");
        phoneMap.put("13422993762", "华贞");
        phoneMap.put("18622382530", "华啟倩");
        phoneMap.put("17645008690", "仲采绿");
        phoneMap.put("15441997997", "卫丹");
        phoneMap.put("17250803381", "戚丽红");
        phoneMap.put("14182855303", "何翠柔");
        phoneMap.put("18369520821", "钱溶艳");
        phoneMap.put("17019156108", "钱琳");
        phoneMap.put("16388355107", "缪静欣");
        phoneMap.put("14464715497", "焦秋菊");
        phoneMap.put("13762892588", "吕访琴");
        phoneMap.put("17062526901", "沈丹");
        phoneMap.put("18478232361", "褚美丽");
    }

    public String produce(){
        init();
        int callerIndex = new Random().nextInt(phoneList.size());
        caller = phoneList.get(callerIndex);
        callerName = phoneMap.get(caller);
        while (true){
            int calleeIndex = new Random().nextInt(phoneList.size());
            callee = phoneList.get(calleeIndex);
            calleeName = phoneMap.get(callee);
            if(!caller.equals(callee)) break;
        }
        callTime = getTime(start, end);
        int randomTime = new Random().nextInt(60*30);
        DecimalFormat df = new DecimalFormat("0000");
        duration = df.format(randomTime);
        log = caller+","+callee+","+callTime+","+duration ;
        return log;
    }
    public String getTime(String start,String end){
        //随机日期：start+(end-start)*new Random()
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = sdf.parse(start).getTime();
            long endTime = sdf.parse(end).getTime();
            long date = startTime + (long)((endTime-startTime)*Math.random());
            String time = sdf.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void writeLog(String logFile){
        while(true){
            String logIn = produce();
            System.out.println(logIn);
            try {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(logFile,true),"UTF-8");
                osw.write(logIn+"\n");
                osw.flush();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        if (args == null || args.length <= 0) {
            System.out.println("no logPath");
            return;
        }
        ProductLog pl = new ProductLog();
        pl.writeLog(args[0]);
    }
}
