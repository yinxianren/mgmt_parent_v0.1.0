package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.MerchantRegisterCollectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantRegisterCollectMapper {

    int countByExample(MerchantRegisterCollectExample example);

    int deleteByExample(MerchantRegisterCollectExample example);

    int deleteByPrimaryKey(String id);

    int insert(MerchantRegisterCollect record);

    int insertSelective(MerchantRegisterCollect record);

    List<MerchantRegisterCollect> selectByExample(MerchantRegisterCollectExample example);

    List<MerchantRegisterCollect> selectByMeridAndterminalMerIdAndStatus(@Param("merId")String merId,@Param("terminalMerId") String terminalMerId,@Param("status")Integer status);

    List<MerchantRegisterCollect> selectByWhereCondition(MerchantRegisterCollect record);

    List<MerchantRegisterCollect> selectByMerOrderIdAndMerId(@Param("merId")String merId,@Param("merOrderId")String merOrderId);

    MerchantRegisterCollect selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MerchantRegisterCollect record, @Param("example") MerchantRegisterCollectExample example);

    int updateByExample(@Param("record") MerchantRegisterCollect record, @Param("example") MerchantRegisterCollectExample example);

    int updateByPrimaryKeySelective(MerchantRegisterCollect record);

    int updateByPrimaryKey(MerchantRegisterCollect record);

    int updateByMerOrderIdAndMerId(MerchantRegisterCollect record);

    List<MerchantRegisterCollect> getmMrchantRegisterCollectByMerIdAndTerminalMerId(@Param("merId")String merId,@Param("terminalMerId") String terminalMerId,@Param("cardNum") String cardNum);

    MerchantRegisterCollect searchMerchantRegisterCollect(@Param("merId")String merId,@Param("terminalMerId") String terminalMerId,@Param("bankCardNum") String bankCardNum,@Param("extraChannelId") String extraChannelId);

    MerchantRegisterCollect getMerchantRegisterCollectbyParam(@Param("extraChannelId")String extraChannelId, @Param("merId")String merId, @Param("merOrderId")String merOrderId);
}
