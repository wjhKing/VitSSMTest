package com.vit.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 对象映射工具
 * Created by huangguoping.
 */
public class ObjecCloneHelper {
    public static void map(Object src, Object target){
        Field[] srcFields = src.getClass().getDeclaredFields();
        for (Field f : srcFields){
            try {
                String genFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                String getMethodName = "get" + genFieldName;
                String setMethodName = "set" + genFieldName;
                //System.out.println("genFieldName = " + genFieldName);
                Method getMethod = src.getClass().getDeclaredMethod(getMethodName);
                Method setMethod = target.getClass().getDeclaredMethod(setMethodName, f.getType());
                setMethod.invoke(target, getMethod.invoke(src));
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
            } catch (IllegalAccessException e) {
                //e.printStackTrace();

            }
        }
    }
}
