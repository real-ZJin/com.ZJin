package com.ZJin.test;

import com.ZJin.hbase.InitHbase;

public class InitHbaseTest {
    public static void main(String[] args) throws Exception{
//        InitHbase.createNamespace();
        InitHbase.createNamespace();
        InitHbase.createRCEmail();
        InitHbase.CreateContent();
        InitHbase.createRelations();
    }
}
