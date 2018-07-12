package com.vit.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by huangguoping on 15/4/28.
 */
public class JsonSerializer {
    static ObjectMapper mapper = new ObjectMapper();
    public static String toJson(Object object){
        try {
            String value_str = mapper.writeValueAsString(object);
            return value_str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T>T from(String json, Class<T> type){
        try {
            T t = mapper.readValue(json, type);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JsonNode readToNode(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {

        }
        return null;
    }
    public static String getNodeValue(JsonNode node) {
        return node == null ? "" : node.asText();
    }
}
