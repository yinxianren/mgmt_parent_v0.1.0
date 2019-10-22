package com.rxh.payInterface;

import com.rxh.exception.NewPayException;

public interface NewPayAssert {

    /**
     *
     * @param bl
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isTrue(boolean bl,String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(bl){
            throw new NewPayException(code,innerPrintMsg,responseMsg);
        }
    }
    /**
     *
     * @param cs
     * @return
     */
    default void isBlank(final CharSequence cs,String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            throw new NewPayException(code,innerPrintMsg,responseMsg);
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return ;
            }
        }
        throw new NewPayException(code,innerPrintMsg,responseMsg);
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
    /**
     * 空对象抛出异常
     * @param object
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isNull(Object object, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null == object)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }

    default boolean isNull(Object object){
        if(null == object) return true;
        return false;
    }


    /**
     * 空对象抛出异常
     * @param object
     * @param code
     * @param innerPrintMsg
     * @param responseMsg
     * @throws NewPayException
     */
    default void isNotNull(Object object, String code, String innerPrintMsg, String responseMsg) throws NewPayException {
        if(null != object)
            throw new NewPayException(code,innerPrintMsg,responseMsg);
    }
}
