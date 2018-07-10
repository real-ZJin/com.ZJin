package com.ZJin.toMysqlFromHbase.uilts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InsertData {
    static private Map<String,String> phoneMap = new HashMap<>();
    static {
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
    public static void main(String[] args) throws Exception{
        //配置数据库信息
        String driverclass = "com.mysql.jdbc.Driver" ;
        String url = "jdbc:mysql://localhost:3306/mydb2018" ;
        String username= "root" ;
        String password = "root" ;

        Class.forName(driverclass);

        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into users values(?,?)";
        PreparedStatement preps = connection.prepareStatement(sql);
        Set<Map.Entry<String, String>> entries = phoneMap.entrySet();
        for(Map.Entry<String,String> map : entries){
            String number = map.getKey();
            String name = map.getValue();
            preps.setString(1,number);
            preps.setString(2,name);
            preps.addBatch();
        }
        preps.executeBatch();
    }
}
