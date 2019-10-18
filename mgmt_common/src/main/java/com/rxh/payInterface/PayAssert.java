package com.rxh.payInterface;

import com.rxh.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 *   断言接口
 * @author panda
 * @date 20190718
 */
public interface PayAssert {

     Logger logger = LoggerFactory.getLogger(PayAssert.class);

    /**
     *
     * @param cs
     * @return
     */
    default void isBlank(final CharSequence cs,String message) throws PayException{
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            throw new PayException(message);
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return ;
            }
        }
        throw new PayException(message);
    }
    default boolean isBlank(final CharSequence cs) {
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


    default  void state(boolean expression, String message,String resultCode)throws PayException  {
        if (!expression) {
            throw new PayException(message,resultCode);
        }
    }

    default  void state(boolean expression, String message)throws PayException  {
        if (!expression) {
            throw new PayException(message);
        }
    }

    /**
     *  空对象抛出异常
     * @param object
     * @param message
     * @throws PayException
     */
    default void isNull(Object object, String message) throws PayException {
        if(null == object)
            throw new PayException(message);
    }

    /**
     *  空对象抛出异常
     * @param object
     * @param message
     * @throws PayException
     */
    default void isNull(Object object, String message,String resultCode) throws PayException {
        if(null == object)
            throw new PayException(message,resultCode);
    }

    /**
     *  不为空则抛出异常
     * @param object
     * @param message
     * @throws PayException
     */
    default void isNotNull(Object object, String message) throws PayException {
        if(null != object)
            throw new PayException(message);
    }
    /**
     *  不为空则抛出异常
     * @param object
     * @param message
     * @throws PayException
     */
    default void isNotNull(Object object, String message,String resultCode) throws PayException {
        if(null != object)
            throw new PayException(message,resultCode);
    }
    /**
     *  判断集合对象是没有元素
     * @param collection
     * @param message
     * @throws PayException
     */
    default void isNotElement(Collection<?> collection, String message) throws PayException {
        if(isNull(collection)){
            throw new PayException(message);
        }

        if(collection.size()==0){
            throw new PayException(message);
        }

    }

    /**
     *  判断集合对象是没有元素
     * @param collection
     * @param message
     * @throws PayException
     */
    default void isNotElement(Collection<?> collection, String message,String resultCode) throws PayException {
        if(isNull(collection))
            throw new PayException(message,resultCode);
        if(collection.size()==0)
            throw new PayException(message,resultCode);
    }

    default void isNotElement(Map<String, Object> map, String message) throws PayException {
        if(isNull(map))
            throw new PayException(message);
        if(map.size()==0)
            throw new PayException(message);
    }

    default void isNotElement(Map<String, Object> map, String message,String resultCode) throws PayException {
        if(isNull(map))
            throw new PayException(message,resultCode);
        if(map.size()==0)
            throw new PayException(message,resultCode);
    }
    /**
     *  判断集合对象有元素
     * @param collection
     * @param message
     * @throws PayException
     */
    default void isHasElement(Collection<?> collection, String message) throws PayException {
        if(isNull(collection))
            throw new PayException(message);
        if(collection.size()>0)
            throw new PayException(message);
    }

    /**
     *  判断集合对象有元素
     * @param collection
     * @param message
     * @throws PayException
     */
    default void isHasElement(Collection<?> collection, String message,String resultCode) throws PayException {
        if(isNull(collection))
            throw new PayException(message,resultCode);
        if(collection.size()>0)
            throw new PayException(message,resultCode);
    }

    /**
     *  判断对象不是空
     * @param object
     * @return
     */
    default boolean isNotNull(Object object){
        if(null != object)
            return  true;
        else
            return false;
    }

    /**
     *  判断对象是空
     * @param object
     * @return
     */
    default boolean isNull(Object object){
        if(null == object) return true;
        else return false;
    }

    /**
     *  判断集合对象有元素
     * @param collection
     * @throws PayException
     */
    default boolean isHasElement(Collection<?> collection) {
        if(isNull(collection) || collection.size() ==0 )   return false;
        else  return true;
    }

   default boolean isHasElement(Object[] objArray){
        if(isNull(objArray) || objArray.length ==0  ) return false;
        else return true;
   }
}
