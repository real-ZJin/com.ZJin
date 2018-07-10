package com.ZJin.mysql;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MysqlUitls {
    static SqlSession s ;
    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            //创建会话工厂
            SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
            //创建会话
            s = ssf.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean login(String username,String password){
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        User user = s.selectOne("weibo.selectOne",u);

        if(user==null){
            return false;
        }else {
            return true;
        }
    }

    public static void region(int id,String username,String password){
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setPassword(password);
        s.insert("weibo.insert", u);
        s.commit();
    }
    public static String getIdByUsername(String username,String password){
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        int id = s.selectOne("weibo.selectIdByUsername", u);
        s.commit();
        return String.valueOf(id);
    }
    public static String getNameByid(String id){
        User u = new User();
        u.setId(Integer.parseInt(id));
        String username = s.selectOne("weibo.selectUsernameById", u);
        s.commit();
        return username;
    }
}
