package com.rxh.mapper.square;

import com.rxh.square.pojo.TransAudit;
import com.rxh.square.pojo.TransAuditExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TransAuditMapper {
    int countByExample(TransAuditExample example);

    int deleteByExample(TransAuditExample example);

    int deleteByPrimaryKey(String id);

    int insert(TransAudit record);

    int insertSelective(TransAudit record);

    List<TransAudit> selectByExample(TransAuditExample example);

    TransAudit selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TransAudit record, @Param("example") TransAuditExample example);

    int updateByExample(@Param("record") TransAudit record, @Param("example") TransAuditExample example);

    int updateByPrimaryKeySelective(TransAudit record);

    int updateByPrimaryKey(TransAudit record);

    int selectSuccessOrderCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<TransAudit> selectOrderByParamMap(@Param("paramMap") Map<String, Object> paramMap);

    TransAudit findTransAuditByTransId(@Param("transId") String transId);
}