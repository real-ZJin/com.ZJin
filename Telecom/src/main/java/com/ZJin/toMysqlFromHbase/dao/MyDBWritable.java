package com.ZJin.toMysqlFromHbase.dao;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyDBWritable implements Writable,DBWritable{
    private String number ;
    private String callDate ;
    private int flag;
    private int callTime;
    private int callNum;

    public MyDBWritable() {
    }

    public MyDBWritable(String number, String callDate, int flag, int callTime, int callNum) {
        this.number = number;
        this.callDate = callDate;
        this.flag = flag;
        this.callTime = callTime;
        this.callNum = callNum;
    }

    //序列化
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(number);
        dataOutput.writeUTF(callDate);
        dataOutput.writeInt(flag);
        dataOutput.writeInt(callTime);
        dataOutput.writeInt(callNum);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        number = dataInput.readLine();
        callDate = dataInput.readLine();
        flag = dataInput.readInt();
        callTime = dataInput.readInt();
        callNum = dataInput.readInt();
    }
    //mysql
    @Override
    public void write(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1,number);
        preparedStatement.setString(2,callDate);
        preparedStatement.setInt(3,flag);
        preparedStatement.setInt(4,callTime);
        preparedStatement.setInt(5,callNum);
    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        number = resultSet.getString(1);
        callDate = resultSet.getString(2);
        flag = resultSet.getInt(3);
        callTime = resultSet.getInt(4);
        callNum = resultSet.getInt(5);
    }
}
