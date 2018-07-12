package com.vit.common.exceptions;

/**
 * Created by huangguoping on 14/12/10.
 */
public class NullOrEmptyException extends Exception {
    private String message = "";
    public NullOrEmptyException(){}
    public NullOrEmptyException(String msg){
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}