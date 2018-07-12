package com.vit.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huangguoping on 16/4/21.
 */
public class MD5CheckSum {
    public static String md5(String str){
        try {
            if (str == null) return "";
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder builder = new StringBuilder();
            for(byte cipher : bytes) {
                String toHexStr = Integer.toHexString(cipher & 0xff);
                builder.append(toHexStr.length() == 1 ? "0" + toHexStr : toHexStr);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
        }
        return "";
    }
}
