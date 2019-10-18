package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.MerchantCardExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantCardMapper {

    int countByExample(MerchantCardExample example);

    int deleteByExample(MerchantCardExample example);

    int insert(MerchantCard record);

    int insertSelective(MerchantCard record);

    List<MerchantCard> selectByExample(MerchantCardExample example);

    int updateByExampleSelective(@Param("record") MerchantCard record, @Param("example") MerchantCardExample example);

    int updateByExample(@Param("record") MerchantCard record, @Param("example") MerchantCardExample example);

    List<MerchantCard> select(MerchantCard record);

    MerchantCard search(MerchantCard record);


    int update(@Param("record")MerchantCard record);

    List<MerchantCard> getAllMerchantCard(@Param("merId") String merId,@Param("terminalMerId")String terminalMerId,@Param("status")Integer status);

    MerchantCard searchMerchantCard(@Param("extraChannelId")String extraChannelId,@Param("merId") String merId,@Param("terminalMerId")String terminalMerId );

    MerchantCard selectById(@Param("id") String id);

}