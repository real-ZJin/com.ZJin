package com.ZJin.test;

import com.ZJin.mysql.MysqlUitls;

public class MysqlTest {
    public static void main(String[] args) {
        test4();
    }
    public static void test4(){
        String nameByid = MysqlUitls.getNameByid("1157439376");
        System.out.println(nameByid);
    }
    public static void test3(){
        String id = MysqlUitls.getIdByUsername("ZJin", "root");
        System.out.println(id);
    }
    public static void test1(){
        boolean flag = MysqlUitls.login("zs", "22");
        System.out.println(flag);
    }
    public static void test2(){
        MysqlUitls.region(1,"zs", "22");
    }
}
