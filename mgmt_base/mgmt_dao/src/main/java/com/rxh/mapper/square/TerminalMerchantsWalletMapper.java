package com.rxh.mapper.square;

import com.rxh.square.pojo.TerminalMerchantsWallet;
import com.rxh.square.pojo.TerminalMerchantsWalletExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TerminalMerchantsWalletMapper {
    int countByExample(TerminalMerchantsWalletExample example);

    int deleteByExample(TerminalMerchantsWalletExample example);

    int deleteByPrimaryKey(String id);

    int insert(TerminalMerchantsWallet record);

    int insertSelective(TerminalMerchantsWallet record);

    List<TerminalMerchantsWallet> selectByExample(TerminalMerchantsWalletExample example);

    TerminalMerchantsWallet selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TerminalMerchantsWallet record, @Param("example") TerminalMerchantsWalletExample example);

    int updateByExample(@Param("record") TerminalMerchantsWallet record, @Param("example") TerminalMerchantsWalletExample example);

    int updateByPrimaryKeySelective(TerminalMerchantsWallet record);

    int updateByPrimaryKey(TerminalMerchantsWallet record);

    TerminalMerchantsWallet search(@Param("merId") String merId, @Param("terminalMerId")String terminalMerId);

    List<TerminalMerchantsWallet> getWalletByParam(TerminalMerchantsWallet terminalMerchantsWallet);

    List<TerminalMerchantsWallet> getIds(@Param("merId") String merId);
    // List<TerminalMerchantsWallet> selectAllIdAndName();
}
