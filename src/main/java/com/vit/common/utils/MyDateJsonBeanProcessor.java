package com.vit.common.utils;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import java.util.Date;

/**
 * Created by ZS on 2015/12/23.
 */
public class MyDateJsonBeanProcessor implements JsonBeanProcessor {
    @Override
    public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
        JSONObject jsonObject = null;
        /*System.out.println("processor class name:"+bean.getClass().getName());*/
        if( bean instanceof java.sql.Date ){
            bean = new Date( ((java.sql.Date) bean).getTime() );
        }
        if( bean instanceof java.sql.Timestamp ){
            /*System.out.println("bean timestamp");*/
            bean = new Date( ((java.sql.Timestamp) bean).getTime() );
        }
        if( bean instanceof Date ){
            jsonObject = new JSONObject();
            jsonObject.element("time", ( (Date) bean ).getTime());
        }else{
            jsonObject = new JSONObject( true );
        }
        return jsonObject;
    }
}
