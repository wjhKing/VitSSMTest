package com.vit.common.utils;


import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by paul on 2016/2/4.
 */
public class StringUtil {
    public static DecimalFormat DF_0 = new DecimalFormat("0.0");
    public static DecimalFormat DF_00 = new DecimalFormat("0.00");

    public final static String REX_PRIMARY_DOMAIN_NAME = "[\\w-]+\\.(com|net|org|gov|cc|biz|info|cn|co)\\b(\\.(cn|hk|uk|jp|tw))*";

    // 匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】
    // 匹配这些英文标点符号 空格 ? , . ! + -
    public final static String REX_KEYWORD = "^[\\u4e00-\\u9fa5|\\u3002|\\uff1f|\\uff01|\\uff0c|\\u3001|\\uff1b|\\uff1a|\\u201c|\\u201d|\\u2018|\\u2019|\\uff08|\\uff09|\\u300a|\\u300b|\\u3008|\\u3009|\\u3010|\\u3011|?|,|.|+|-|[\\s\\S]*!a-zA-Z0-9]+$";

    public final static StringBuffer BUFFER = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");

    public final static StringBuffer ID_BUFFER = new StringBuffer("0123456789abcdef");

    //正则表达式，匹配是否日期
    public final static String REX_DATE_YMD = "^\\d{4}\\D\\d{1,2}\\D\\d{1,2}$";

    //验证是否是数字
    public final static String REX_NUMBER = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";

    public static final char UNDERLINE = '_';

    public final static String HTTP = "http://";
    public final static String SLASH = "/";

    /**
     * 匹配是否是日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean rexDateForYMD(String strDate) {
        return strDate.matches(REX_DATE_YMD);
    }

    /**
     * 验证是否数字格式
     *
     * @param value
     * @return
     */
    public static boolean checkNumber(String value) {
        return value.matches(REX_NUMBER);
    }

    /**
     * 生成一个uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 返回百分比(小数点后一位)
     *
     * @return
     */
    public static double getDoublePer(double d) {
        if (d != 0) {
            d = d * 100;
            BigDecimal b = new BigDecimal(d);
            return b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return 0;
    }

    /**
     * 获取随机数
     *
     * @param length
     * @return
     */
    public static String getKeyByLen(int length) {
        if (length > 32) {
            return null;
        }
        String token = StringUtil.uuid().replaceAll("-", "").toUpperCase().substring(0, length);
        return token;
    }

    /**
     * 根据URL得到该URL的顶级域名
     *
     * @param url
     * @return
     */
    public static String getTopDomain(String url) {
        String pDomainName = null;
        if (StringUtils.isBlank(url)) {
            return pDomainName;
        }
        Pattern pattern = Pattern.compile(REX_PRIMARY_DOMAIN_NAME, Pattern.CASE_INSENSITIVE);
        Matcher matcherDomainName = pattern.matcher(url);
        if (matcherDomainName.find()) {
            pDomainName = matcherDomainName.group();
        }
        return pDomainName;
    }

    public static String getDomain(String url) {
        String domain = null;
        if (StringUtils.isBlank(url)) {
            return url;
        }
//        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
//        Pattern p = Pattern.compile("(\\w*\\.?){2}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$");
        Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(url);
        if (m.find()) {
            domain = m.group();
        }
        return domain;
    }

    /**
     * 设置http开头
     *
     * @param url
     * @return
     */
    public static String setHttp(String url) {
        return StringUtils.isNotBlank(url) && !url.startsWith("http") ? "http://" + url : url;
    }

    /**
     * 获取随机码
     *
     * @param length
     * @return
     */
    public static String getRandomKey(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = BUFFER.length();
        for (int i = 0; i < length; i++) {
            sb.append(BUFFER.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * 获取16进制开头的的随机id
     *
     * @param length
     * @return
     */
    public static String getRandomId(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = ID_BUFFER.length();
        for (int i = 0; i < length; i++) {
            sb.append(ID_BUFFER.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * 去除http开头的字符串
     *
     * @param word
     * @return
     */
    public static String removeHttpHeader(String word) {
        if (StringUtils.isNotBlank(word)) {
            word = word.trim();
            if (word.startsWith(HTTP)) {
                word = StringUtils.substringAfter(word, HTTP);
            }
            if (word.endsWith(SLASH)) {
                word = StringUtils.substringBefore(word, SLASH);
            }
        }
        return word;
    }

    /**
     * 判断字符串是否包含数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getPercentageStr(int num1, int num2) {
        if (num1 < 1) return "00.00%";
        double resultDouble = ((double) num1 / (double) num2) * 100;
        DecimalFormat df = new DecimalFormat("#.00");
        String resultStr = df.format(resultDouble);
        return resultStr + "%";

    }

    /**
     * 去除‘’符号
     * @param str
     * @return
     */
    public static String removeSeparator(String str,int type) {
        String string = str;
        if (type == 0) {
            string = str.replaceAll("\"","");
        }
        if (type == 1) {
            string = str.replaceAll("\"","")
                    .replaceAll("\\(","")
                    .replaceAll("\\)","");
        }
        if (type == 2) {
            string = str.replaceAll("\"","")
                    .replaceAll("\'","")
                    .replaceAll("\\\\","")
                    .replaceAll("\\.","");
        }
        return string;
    }

    /**
     * 全角字符串转换半角字符串
     *
     * @param fullWidthStr
     *            非空的全角字符串
     * @return 半角字符串
     */
    public static String fullWidth2halfWidth(String fullWidthStr) {
        if (null == fullWidthStr || fullWidthStr.length() <= 0) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }
}
