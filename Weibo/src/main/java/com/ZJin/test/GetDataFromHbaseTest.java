package com.ZJin.test;

import com.ZJin.hbase.Content;
import com.ZJin.hbase.GetDataFromHbase;

import java.util.List;

public class GetDataFromHbaseTest {
    public static void main(String[] args) throws Exception{
        test2();
    }
    private static void test2() throws Exception{
        List<Content> content = GetDataFromHbase.getContentById("0002");
        for(Content s:content){
            System.out.println(s.getContent());
        }
    }
    private static void test1() throws Exception{
        GetDataFromHbase.getfans("0002");
    }
}
