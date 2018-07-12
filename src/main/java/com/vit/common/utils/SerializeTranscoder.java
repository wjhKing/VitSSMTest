package com.vit.common.utils;

import java.io.Closeable;

/**
 * Created by ZS on 2016/1/19.
 */
public abstract class SerializeTranscoder {

    public abstract byte[] serialize(Object value);

    public abstract Object deserialize(byte[] in);

    public void close(Closeable closeable){
        if(closeable != null){
            try{
                closeable.close();
            }catch (Exception e){

            }
        }
    }
}
