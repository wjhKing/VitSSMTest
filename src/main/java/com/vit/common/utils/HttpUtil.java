package com.vit.common.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangguoping.
 */
public class HttpUtil {

    public static Resp httpGet(String url, Map<String,String> params, Map<String, String> headers, String encode) throws IOException {

        HttpClient client = HttpClients.createDefault();

        StringBuilder stringBuilder = new StringBuilder();
        if (params != null && params.size() > 0){
            for (Map.Entry<String, String> entry : params.entrySet()){
                if (entry.getValue() == null || entry.getValue().equals("")) continue;
                try {
                    stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                }
                stringBuilder.append("&");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length() - 1, "&");
        }
        StringBuilder stringBuilder_url = new StringBuilder();
        if (url.contains("?")){
            if (url.endsWith("?")) stringBuilder_url.append(url).append(stringBuilder);
            else if (url.endsWith("&")) stringBuilder_url.append(url).append(stringBuilder);
            else stringBuilder_url.append(url).append("&").append(stringBuilder);
        }else{
            stringBuilder_url.append(url).append("?").append(stringBuilder);
        }

        HttpGet hget = new HttpGet(stringBuilder_url.toString());
        if (headers != null && headers.size() > 0){
            for (Map.Entry<String, String> entry : headers.entrySet()){
                if (entry.getValue() == null || entry.getValue().equals("")) continue;
                hget.addHeader(entry.getKey(), entry.getValue());
            }
        }

        HttpResponse response = client.execute(hget);
        Resp resp = new Resp();
        resp.code = response.getStatusLine().getStatusCode();
        if (resp.code == 200){
            HttpEntity entity = response.getEntity();
            Header contentEncode = entity.getContentEncoding();
            if (contentEncode != null) encode = contentEncode.getValue();
            resp.content = EntityUtils.toString(entity, encode);
        }
        return resp;
    }

    public static Resp httpGet(String url, Map<String,String> params) throws IOException {
        return httpGet(url, params, null, "utf-8");
    }

    public static Resp httpPost(String url, Map<String,String> params, Map<String, String> headers, String encode) throws IOException {
        HttpClient client = HttpClients.createDefault();

        HttpPost hPost = new HttpPost(url);
        if (headers != null && headers.size() > 0){
            for (Map.Entry<String, String> entry : headers.entrySet()){
                if (entry.getValue() == null || entry.getValue().equals("")) continue;
                hPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (params != null && params.size() > 0){
            List<BasicNameValuePair> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()){
                if (entry.getValue() == null || entry.getValue().equals("")) continue;
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            hPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
        }

        HttpResponse response = client.execute(hPost);
        Resp resp = new Resp();
        resp.code = response.getStatusLine().getStatusCode();
        if (resp.code == 200){
            HttpEntity entity = response.getEntity();
            Header contentEncode = entity.getContentEncoding();
            if (contentEncode != null) encode = contentEncode.getValue();
            resp.content = EntityUtils.toString(entity, encode);
        }
        return resp;
    }

    public static Resp httpPost(String url, Map<String,String> params) throws IOException {
        return httpPost(url, params, null, "utf-8");
    }

    public static class Resp {
        public int code;
        public String content;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
