package com.vit.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZS on 2016/1/19.
 */
public class ListTranscoder<M extends Serializable> extends SerializeTranscoder {
    @SuppressWarnings("unchecked")
    @Override
    public byte[] serialize(Object value) {
        if(value == null){
            throw new NullPointerException("Can't serialize null");
        }
        List<M> values = (List<M>)value;

        byte[] results = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;

        try{
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            for(M m:values){
                os.writeObject(m);
            }
            os.close();
            bos.close();
            results = bos.toByteArray();
        }catch (IOException e){
            throw new IllegalArgumentException("Non-serializable object",e);
        }finally {
            close(os);
            close(bos);
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object deserialize(byte[] in) {
        List<M> list = new ArrayList<>();

        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try{
            if(in != null){
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                while(true){
                    M m = (M)is.readObject();
                    if(m == null){
                        break;
                    }
                    list.add(m);
                }
                is.close();
                bis.close();
            }
        }catch (IOException e){

        }catch (ClassNotFoundException e){
            
        }

        return list;
    }
}
