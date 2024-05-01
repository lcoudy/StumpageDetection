package com.stumdet.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于给数据库生成DateTime格式的时间
 */
public class SqlDateTimeProducer {

    // 获取当前时间
    public static Timestamp getCurrentDateTime(){
        return new Timestamp(new Date().getTime());
    }

    //转换成Timestamp
    public static Timestamp toTimestamp(long stamp){
        return new Timestamp(stamp);
    }

    public static Timestamp toTimestamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static Timestamp toTimestamp(String dateStr){
        // 日期格式： yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            System.out.println("[Error] Timestamp SqlDateTimePorducer.toTimestamp(String) 时间格式转换失败");
        }
        return null;
    }
}
