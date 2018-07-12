package com.vit.common.utils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by huangguoping on 15/4/24.
 */
public class DateHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static int  compareTo(Date date1, Date date2){
        if (Checker.isNone(date1) && Checker.isNone(date2)){
            return 0;
        }
        if (!Checker.isNone(date1) && !Checker.isNone(date2)){
            return date1.compareTo(date2);
        }else {
            if (Checker.isNone(date1)){
                return -1;
            }
            if (Checker.isNone(date2)){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 往前/后增加天
     * @param days
     * @return
     */
    public static Date addDays(Date dNow,int days){
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.DAY_OF_YEAR, days);
        dBefore = calendar.getTime();
        return dBefore;
    }

    /**
     * 往前/后增加月份
     * @param months
     * @return
     */
    public static String addMonths(int months){
        Date dNow = new Date(); //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, months);
        dBefore = calendar.getTime();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore); //格式化前3月的时间

        return defaultStartDate;
    }

    /**
     * 往前/后增加天
     * @param days
     * @return
     */
    public static Date addDays(int days){
        Date dNow = new Date(); //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.DAY_OF_YEAR, days);
        dBefore = calendar.getTime();
        return dBefore;
    }

    /**
     * 日期转换
     * @param date
     * @return
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }

    /**
     * 日期转换为时间戳
     * @param xmlCalendar
     * @return
     */
    public static long convertXMLCalendarToLong(XMLGregorianCalendar xmlCalendar){
        if(xmlCalendar==null)
            return 0;

        try{
            GregorianCalendar ca = xmlCalendar.toGregorianCalendar();
            return ca.getTimeInMillis();}
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 数据类型转换
     * @param cal
     * @return
     * @throws Exception
     */
    public static Date convertToDate(XMLGregorianCalendar cal) {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }

    public static String formatDate(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *
     * <p>
     * Title: formatDate
     * </p>
     * <p>
     * Description: 将传入的字符串转换为日期类型
     * </p>
     *
     * @param s
     *            传入日期格式的字符串
     * @return Date 格式化后的日期
     */
    public static Date formatDate(String s) {
        Date d = null;
        try {
            d = dateFormat.parse(s);
        } catch (Exception localException) {
        }
        return d;
    }

    /**
     * @Title: formatDate
     * @Description: 将传入的字符串，按照传入的指定格式 格式化，转换为日期类型
     * @param s 日期格式的字符串
     * @param format 指定的日期格式
     * @return date 格式化后的日期
     */
    public static Date formatDate(String s, String format) {
        Date d = null;
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat(format);
            d = dFormat.parse(s);
        } catch (Exception localException) {
        }
        return d;
    }
}
