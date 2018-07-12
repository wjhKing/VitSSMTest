package com.vit.common.base;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    public static final int SUCCEED = 0;//成功
    /**
     * 参数错误
     */
    public static final int PARAMETER_ERROR = 1;//参数错误
    /**
     * 签名错误
     */
    public static final int SIGN_EEROR = 2;//签名错误
    /**
     * 应用级别错误
     */
    public static final int APP_ERROR = 3;//应用级别错误
    /**
     * 应用级别异常
     */
    public static final int APP_EXCEPTION = 4;//应用级别异常
    /**
     * 其他错误
     */
    public static final int OTHER_ERROR = 5;//其他错误

    protected int code;
    protected String info;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public BaseResponse setInfo(String info) {
        this.info = info;
        return this;
    }
}
