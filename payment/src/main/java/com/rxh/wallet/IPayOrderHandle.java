package com.rxh.wallet;

import java.io.Serializable;
import java.util.Collection;

/**
 *  处理器
 * @author  panda
 * @date 20190721
 * @param <T>
 */
public interface IPayOrderHandle<T extends Serializable> {
    void payOrderToObject(T t);
    void payOrderToToCollect(Collection<T> collection);
}

