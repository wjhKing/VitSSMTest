package com.vit.common.base;

import java.io.Serializable;

public class ApiDataResponse<T> extends BaseResponse implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public ApiDataResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public ApiDataResponse() {
    }

    public ApiDataResponse(T data,int code,String info) {
        this.data = data;
        super.setCode(code);
        super.setInfo(info);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public ApiDataResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public ApiDataResponse<T> setInfo(String info) {
        this.info = info;
        return this;
    }
}
