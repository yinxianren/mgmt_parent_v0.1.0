package com.rxh.mapper.square;


import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.square.pojo.SystemOrderTrackExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SystemOrderTrackMapper {
    int countByExample(SystemOrderTrackExample example);

    int deleteByExample(SystemOrderTrackExample example);

    int deleteByPrimaryKey(String id);

    int insert(SystemOrderTrack record);

    int insertSelective(SystemOrderTrack record);

    List<SystemOrderTrack> selectByExampleWithBLOBs(SystemOrderTrackExample example);

    List<SystemOrderTrack> selectByExample(SystemOrderTrackExample example);

    SystemOrderTrack selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SystemOrderTrack record, @Param("example") SystemOrderTrackExample example);

    int updateByExampleWithBLOBs(@Param("record") SystemOrderTrack record, @Param("example") SystemOrderTrackExample example);

    int updateByExample(@Param("record") SystemOrderTrack record, @Param("example") SystemOrderTrackExample example);

    int updateByPrimaryKeySelective(SystemOrderTrack record);

    int updateByPrimaryKeyWithBLOBs(SystemOrderTrack record);

    int updateByPrimaryKey(SystemOrderTrack record);

    SystemOrderTrack getSystemOrderTrack(String orderId);

    List<SystemOrderTrack> selectAllSystemOrder(@Param("paramMap") Map<String, Object> paramMap);

    int selectSuccessOrderCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<SystemOrderTrack> getIds();

    /**
     * 查询成功或失败的订单
     * @param orderId
     * @return
     */
    SystemOrderTrack findOneByMerOrderId(@Param("ordedrId") String orderId);
}