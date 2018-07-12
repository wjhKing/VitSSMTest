package com.vit.common.utils;

import java.util.*;
import java.util.function.Function;

public class CollectionUtil {

    public static <T> List<T> toList(Iterable<T> iterable) {
        if (iterable == null) return null;
        List<T> ts = new LinkedList<>();
        iterable.forEach(t1 -> ts.add(t1));
        return ts;
    }

    public static <S, T> List<T> toList(Iterable<S> iterable, Function<S, T> conver) {
        if (iterable == null) return null;
        List<T> ts = new LinkedList<>();
        for (S s : iterable){
            ts.add(conver.apply(s));
        }
        return ts;
    }

    public static <S, T> List<T> toList(List<S> list, Function<S, T> conver) {
        if (list == null) return null;
        List<T> ts = new LinkedList<>();
        for (S s : list){
            ts.add(conver.apply(s));
        }
        return ts;
    }

    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> keyFunc) {
        if (list == null) return null;
        Map<K, V> map = new HashMap<K, V>();
        list.stream().forEach(v -> map.put(keyFunc.apply(v), v));
        return map;
    }

    public static <K, V> Map<K, V> toMap(V[] arr, Function<V, K> keyFunc) {
        if (arr == null) return null;
        Map<K, V> map = new HashMap<K, V>();
        for (V v : arr){
            map.put(keyFunc.apply(v), v);
        }
        return map;
    }

    public static <K, V> Map<K, V> toMap(Iterable<V> iterable, Function<V, K> keyFunc) {
        if (iterable == null) return null;
        Map<K, V> map = new HashMap<K, V>();
        iterable.forEach(v -> map.put(keyFunc.apply(v), v));
        return map;
    }

    public static <K, V> Map<K, V> toMap(Iterator<V> iterator, Function<V, K> keyFunc) {
        if (iterator == null) return null;
        Map<K, V> map = new HashMap<K, V>();
        while (iterator.hasNext()){
            V v = iterator.next();
            map.put(keyFunc.apply(v), v);
        }
        return map;
    }

    public static <T, V> Set<V> toSet(List<T> list, Function<T, V> vFun){
        if (list == null) return null;
        Set<V> set = new HashSet<V>();
        for (T t : list){
            set.add(vFun.apply(t));
        }
        return set;
    }

    public static <T, V> Set<V> toSet(Set<T> s, Function<T, V> vFun){
        if (s == null) return null;
        Set<V> set = new HashSet<V>();
        for (T t : s){
            set.add(vFun.apply(t));
        }
        return set;
    }

    /**
     * 通过hashSet去除重复的list数据
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        Set set = new  HashSet();
        List newList = new  ArrayList();
        for (T cd:list) {
            if(set.add(cd)){
                newList.add(cd);
            }
        }
        return newList;
    }

    /**
     * 数组去重
     * @param array
     * @param <T>
     * @return
     */
    public static <T> String[] removeDuplicateOfArray(String[] array) {
        if (array != null && array.length > 0) {
            Set set = new HashSet();
            List<T> newList = new  ArrayList();
            for (int i = 0; i < array.length; i++) {
                if (set.add(array[i])) {
                    newList.add((T) array[i]);
                }
            }
            String[] newArray = new String[newList.size()];
            array = newList.toArray(newArray);
        }
        return array;
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source,int n){
        List<List<T>> result=new ArrayList<List<T>>();
        //(先计算出余数)
        int remaider=source.size()%n;
        //然后是商
        int number=source.size()/n;
        //偏移量
        int offset=0;
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }
}
