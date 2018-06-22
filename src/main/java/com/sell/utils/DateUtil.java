package com.sell.utils;



import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil，时间操作类
 *
 * @author 司徒彬
 * @date 2016年10月27日13:50:56
 */
public class DateUtil
{

    /**
     * 获得当前时间
     *
     * @return the date
     */
    public static Timestamp getNow()
    {
        return new Timestamp(System.currentTimeMillis());
    }





    /**
     * 获取数据查询日期，格式是：2014-04-02，day是天差，如果传-1是昨天的日期
     *
     * @author：ErebusST @date：2017/4/25 14:19
     */
    public static Timestamp addDay(int day)
    {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, day);
        Date date = cal.getTime();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        return currentTimestamp;
    }

    public static Timestamp addDay(Timestamp date, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.DATE, day);
        Date result = cal.getTime();
        Timestamp currentTimestamp = new Timestamp(result.getTime());
        return currentTimestamp;
    }


}
