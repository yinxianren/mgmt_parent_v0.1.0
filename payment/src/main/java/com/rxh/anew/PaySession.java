package com.rxh.anew;

import com.rxh.tuple.Tuple2;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午3:26
 * Description:
 */


public class PaySession {

    private static  long  t0 = 86400000*3;//3d
    private static Map<String, Tuple2<Long,Object>> session = new ConcurrentHashMap(32);
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    public synchronized static Object  put(String key, Object object,Long timestamp){
        if(isBlank(key) || null == object || null == timestamp) return null;
        object = session.put(key,new Tuple2<>(timestamp,object));
        pool.submit(()->clear());
        return object;
    }


    public synchronized  static Object getAndRemove(String key){
        if(isBlank(key)) return null;
        Tuple2<Long,Object> tuple2 =  session.remove(key);
        return  tuple2._2;
    }


    public synchronized static void clear(){
        Set<String> keySet = session.keySet();
        long t1 = System.currentTimeMillis();
        for(String key : keySet){
            Tuple2<Long,Object> tuple2 = session.get(key);
            long t2 = t1 - tuple2._1;
            if( t2 >= t0 ){
                session.remove(key);
            }
        }
    }


    public static int size(){
        return session.size();
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
