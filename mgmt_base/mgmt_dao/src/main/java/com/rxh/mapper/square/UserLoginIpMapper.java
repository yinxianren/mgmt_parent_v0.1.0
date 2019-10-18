package com.rxh.mapper.square;

import com.rxh.pojo.base.SearchInfo;
import com.rxh.square.pojo.UserLoginIp;
import com.rxh.square.pojo.UserLoginIpExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserLoginIpMapper {
    int countByExample(UserLoginIpExample example);

    int deleteByExample(UserLoginIpExample example);



    int insert(UserLoginIp record);

    int insertSelective(UserLoginIp record);

    List<UserLoginIp> selectByExample(UserLoginIpExample example);

    int updateByExampleSelective(@Param("record") UserLoginIp record, @Param("example") UserLoginIpExample example);

    int updateByExample(@Param("record") UserLoginIp record, @Param("example") UserLoginIpExample example);

    List<UserLoginIp> selectBySearchInfo(SearchInfo searchInfo);

    List<String> searchByCustomerId(String customerId);
    int deleteByPrimaryKey(String id);
}