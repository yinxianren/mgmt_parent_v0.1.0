package com.rxh.service.base;



import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 服务接口类
 * </pre>
 *
 * @author xuzm
 * @version 1.00
 */
public interface BaseServiceXZM<E, PK> {
    /**
     * 保存
     *
     * @param entity
     * @return
     */
    public Map<String, Object> save(E entity);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    public int deleteById(PK id);

    /**
     * 根据查询条件删除
     *
     * @param param
     * @return
     */
    public int deleteByParam(Map<String, Object> param);

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    public Map<String,Object> update(E entity);

    /**
     * 根据主键更新不为空的字段
     *
     * @param entity
     * @return
     */
    public int updateByIdSelective(E entity);

    /**
     * 根据主键查询对象
     *
     * @param id
     * @return
     */
    public E selectById(PK id);

    /**
     * 根据条件查询结果集,包括传入分页参数
     *
     * @param param
     * @return
     */
    public List<E> queryList(Map<String, Object> param);

}
