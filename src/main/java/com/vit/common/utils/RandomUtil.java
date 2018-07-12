package com.vit.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjianhui on 2017/12/21.
 * 随机数工具类
 */
public class RandomUtil {

    /**
     * 获取在指定范围内的随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInMinToMax(int min, int max) {
        int randomNum = 0;
        if (min < 0 || min >= max) {
            return randomNum;
        }
        Random random = new Random();
        randomNum = random.nextInt(max)%(max-min+1) + min;
        return randomNum;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        // 获取5位随机数
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
        return str + rannum;// 当前时间
    }

    /**
     * 检测字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 调用linux的urandom,生成168位随机的字符串
     */
    public static String generateThirdSessionId() {
//        return exec("head -n 80 /dev/urandom | tr -dc A-Za-z0-9 | head -c 168");
        return UUID.randomUUID().toString();
    }
}
