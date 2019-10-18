package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantsDetails;
import com.rxh.square.pojo.TerminalMerchantsDetails;
import com.rxh.square.pojo.TerminalMerchantsDetailsExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TerminalMerchantsDetailsMapper {
    int countByExample(TerminalMerchantsDetailsExample example);

    int deleteByExample(TerminalMerchantsDetailsExample example);

    int deleteByPrimaryKey(String id);

    int insert(TerminalMerchantsDetails record);

    int insertSelective(TerminalMerchantsDetails record);

    List<TerminalMerchantsDetails> selectByExample(TerminalMerchantsDetailsExample example);

    TerminalMerchantsDetails selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TerminalMerchantsDetails record, @Param("example") TerminalMerchantsDetailsExample example);

    int updateByExample(@Param("record") TerminalMerchantsDetails record, @Param("example") TerminalMerchantsDetailsExample example);

    int updateByPrimaryKeySelective(TerminalMerchantsDetails record);

    int updateByPrimaryKey(TerminalMerchantsDetails record);

    List<TerminalMerchantsDetails> selectTerminalMerchantsDetails(@Param("paramMap") Map<String, Object> paramMap);

    int countTerminalMerchantsDetails(@Param("paramMap") Map<String, Object> paramMap);

    List<TerminalMerchantsDetails> getIds();

    TerminalMerchantsDetails searchByOrderId(String orderId);
}