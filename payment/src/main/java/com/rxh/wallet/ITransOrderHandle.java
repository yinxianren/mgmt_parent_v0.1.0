package com.rxh.wallet;

import java.io.Serializable;
import java.util.Collection;

/**
 *  处理器
 * @author  panda
 * @date 20190729
 * @param <T>
 */
public interface ITransOrderHandle<T extends Serializable> {
    void transOrderToObject(T t);
    void transOrderToToCollect(Collection<T> collection);
}
