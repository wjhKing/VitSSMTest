package com.vit.common.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ZS on 2015/12/23.
 */
public class JsonUtils {
    /**
     * 字符串反序列化为实体类，针对单个实体类，不包括自定义类型，
     * 比如Topic类，List<Topic>类
     * @param source
     * @param beanClass
     * @return
     */
    public static Object getObjFromJson(String source,Class beanClass){
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(source);
        return JSONObject.toBean(jsonObject,beanClass);
    }

    /**
     * 字符串反序列化为实体类，针对单个实体类，包括自定义类型，
     * @param sourceArrStr
     * @param beanClass
     * @param classMap map.put("topic",Topic.class)
     * 例子:
     *     DoctorIndexView item = new DoctorIndexView();
     *     List<DoctorPageItem.class> list = new Array<DoctroPageItem>();
     *     list.add(new DoctorPageItem());
     *     item.setViewList(list);
     *     String json = JsonUtil.getJsonStrFromObj(item);
     *
     *     DoctorIndexView temp = null;
     *     Map map = new HashMap();
     *     map.put("viewList",DoctorIndexPageItem.class);
     *     temp = (DoctorIndexView) JsonUtil.getObjFromJsonArrayStr(json,DoctorIndexView.class,map);
     * @return
     */
    public static Object getObjFromJson(String sourceArrStr, Class beanClass, Map classMap){
        JSONObject jsonObj = JSONObject.fromObject(sourceArrStr);
        return JSONObject.toBean(jsonObj, beanClass, classMap);
    }

    /**
     * json转换成list bean,不包括自定义类型
     * @param sourceArrStr
     * @param beanClass
     * @return
     */
    public static List getListFromJsonArrStr(String sourceArrStr, Class beanClass){
        if(StringUtils.isEmpty(sourceArrStr)){
            return null;
        }
        JSONArray jsonArray = JSONArray.fromObject(sourceArrStr);
        List list = new ArrayList();
        for(int i = 0 ; i < jsonArray.size() ; i++){
            list.add(JSONObject.toBean(jsonArray.getJSONObject(i),beanClass));
        }
        return list;
    }

    /**
     * json转换list bean 包括自定义实体类，如List<Topic>
     * @param sourceArrStr
     * @param beanClass
     * @param classMap map.put("topic",new Topic().getClass())
     * @return
     */
    public static List getListFromJsonArrStr(String sourceArrStr,Class beanClass, Map classMap){
        JSONArray jsonArray = JSONArray.fromObject(sourceArrStr);
        List list = new ArrayList();
        for(int i = 0 ; i < jsonArray.size() ; i++){
            list.add(JSONObject.toBean(jsonArray.getJSONObject(i),beanClass,classMap));
        }
        return list;
    }

    /**
     * 对象json序列化
     * @param obj
     * @return
     */
    public static String getJsonStrFromObj(Object obj){
        /*return JsonSerializer.toJson(obj);*/
        //返回結果
        String jsonStr = null;
        //判空
        if (obj == null) {
            return "{}";
        }
        //Json配置
        JsonConfig jsonCfg = new JsonConfig();

        //注册日期处理器
        jsonCfg.registerJsonBeanProcessor(java.sql.Timestamp.class,
                new MyDateJsonBeanProcessor());

        //判断是否是list
        if (obj instanceof Collection || obj instanceof Object[]) {
            jsonStr = JSONArray.fromObject(obj, jsonCfg).toString();
        } else {
            jsonStr = JSONObject.fromObject(obj, jsonCfg).toString();
        }
        return jsonStr;
    }
}
