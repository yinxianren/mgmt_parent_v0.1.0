package com.rxh.service.impl;

import com.rxh.mapper.square.*;
import com.rxh.mapper.sys.SysUserMapper;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantWalletService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MerchantWalletServiceImpl implements MerchantWalletService {

    @Resource
   private MerchantWalletMapper merchantWalletMapper;

    @Resource
    private MerchantSquareInfoMapper merchantSquareInfoMapper;
    @Resource
    private FinanceDrawingSquareMapper financeDrawingSquareMapper;
    @Resource
    private MerchantsDetailsMapper merchantsDetailsMapper;
    @Resource
    private ChannelWalletMapper channelWalletMapper;
    @Resource
    private ChannelDetailsMapper channelDetailsMapper;

    @Override
    public MerchantWallet searchById(String merId) {
        return merchantWalletMapper.selectByPrimaryKey(merId);
    }

    @Override
    public List<MerchantWallet> searchByParam(MerchantInfo param) {
        return merchantWalletMapper.searchByParam(param);
    }



    @Override
    public String getMerchantIdIncre() {
        return merchantWalletMapper.getMerchantIdIncre();
    }

    @Override
    public String getMaxMerId() {
        String maxMerId = merchantWalletMapper.getMaxMerId();
        String id = UUID.createNumber("M",maxMerId);
        return  id ;
       /* int id = UUID.createIntegerKey(maxMerId);
        return String.valueOf(id);*/
    }

    @Override
    public List<MerchantWallet> search(MerchantWallet merchantWallet) {
        return merchantWalletMapper.search(merchantWallet.getMerId());
    }

    @Override
    @Transactional
    public boolean insert(MerchantWallet merchantWallet) {
        MerchantWallet merchant = merchantWalletMapper.selectByPrimaryKey(merchantWallet.getMerId());
        if (merchant != null) {
            BigDecimal balance = merchant.getTotalAvailableAmount();
            if (balance.compareTo(new BigDecimal(0))>0) {
                FinanceDrawing financeDrawing = new FinanceDrawing();
                financeDrawing.setId(UUID.createDrawalNumber(financeDrawingSquareMapper.getMaxDrawingId()));
                financeDrawing.setCustomerId(merchantWallet.getMerId());
                financeDrawing.setDrawingAmount(merchantWallet.getWithdrawalAmount());
                financeDrawing.setStatus(0);
                financeDrawing.setApplicationTime(new Date());
                financeDrawing.setApplicant(merchantSquareInfoMapper.getMerchantName(merchantWallet.getMerId()).getMerchantName());
                int finance = financeDrawingSquareMapper.insertSelective(financeDrawing);
                if (finance != 0){
                    MerchantWallet wallet = new MerchantWallet();
                    BigDecimal availableAmount = merchantWallet.getTotalAvailableAmount().subtract(merchantWallet.getWithdrawalAmount());
                    wallet.setMerId(merchantWallet.getMerId());
                    wallet.setTotalAvailableAmount(availableAmount);
                    int mer = merchantWalletMapper.updateByPrimaryKeySelective(wallet);
                    if (mer != 0) {
                            return true;
                    }
                }
             }
        }
        return false;
    }

    @Override
    @Transactional
    public  int  invest(Object investInfo) {
        LinkedHashMap map = (LinkedHashMap) investInfo;
        MerchantWallet merchant = merchantWalletMapper.selectByPrimaryKey(String.valueOf(map.get("merId")));
        ChannelWallet channelWallet = channelWalletMapper.getChannelWallet(String.valueOf(map.get("channelId")));
        BigDecimal investAmount = new BigDecimal(map.get("amount").toString());//获取充值金额
        BigDecimal totalAmount;
        BigDecimal incomeAmount;
        BigDecimal totalBalance;
        BigDecimal totalAvailableAmount;
        int i =0;
        //更新商户钱包
        if (merchant != null) {
            totalAmount = merchant.getTotalAmount() == null ? new BigDecimal(0) : merchant.getTotalAmount();//获取现有订单总额
            incomeAmount = merchant.getIncomeAmount() == null ? new BigDecimal(0) : merchant.getIncomeAmount();//获取现有入账总额
            totalBalance = merchant.getTotalBalance() == null ? new BigDecimal(0) : merchant.getTotalBalance();//获取现有总余额
            totalAvailableAmount = merchant.getTotalAvailableAmount() == null ? new BigDecimal(0) : merchant.getTotalAvailableAmount();  //获取现有总可用余额
            totalAmount = totalAmount.add(investAmount);
            incomeAmount = incomeAmount.add(investAmount);
            totalBalance = totalBalance.add(investAmount);
            totalAvailableAmount = totalAvailableAmount.add(investAmount);
            merchant.setTotalAmount(totalAmount);
            merchant.setIncomeAmount(incomeAmount);
            merchant.setTotalBalance(totalBalance);
            merchant.setTotalAvailableAmount(totalAvailableAmount);
            merchant.setUpdateTime(new Date());
            i = merchantWalletMapper.updateByPrimaryKeySelective(merchant);
        } else {
            MerchantWallet merchantWallet = new MerchantWallet();
            merchantWallet.setMerId(map.get("merId") + "");
            merchantWallet.setTotalAmount(investAmount);
            merchantWallet.setIncomeAmount(investAmount);
            totalBalance=investAmount;
            merchantWallet.setTotalBalance(investAmount);
            merchantWallet.setTotalAvailableAmount(investAmount);
            merchantWallet.setUpdateTime(new Date());
            i = merchantWalletMapper.insertSelective(merchantWallet);
        }
        String maxOrderId = merchantsDetailsMapper.getMaxMerchantsDetailsOrderId();
        MerchantsDetails merchantsDetails = new MerchantsDetails();
        merchantsDetails.setMerId(String.valueOf(map.get("merId")));
        merchantsDetails.setId(UUID.createKey("merchants_details")+"");
        merchantsDetails.setOrderId(UUID.createKey("merchants_details")+"");
        merchantsDetails.setType("8");
        merchantsDetails.setAmount(investAmount);
        merchantsDetails.setInAmount(investAmount);
        merchantsDetails.setTotalBalance(totalBalance);
        merchantsDetails.setUpdateTime(new Date());
        merchantsDetails.setTimestamp(String.valueOf(new Date().getTime()));
        merchantsDetailsMapper.insertSelective(merchantsDetails);

        //更新平台钱包
        if (channelWallet != null){
            totalAmount = channelWallet.getTotalAmount() == null ? new BigDecimal(0) : channelWallet.getTotalAmount();//获取现有订单总额
            incomeAmount = channelWallet.getIncomeAmount() == null ? new BigDecimal(0) : channelWallet.getIncomeAmount();//获取现有入账总额
            totalBalance = channelWallet.getTotalBalance() == null ? new BigDecimal(0) : channelWallet.getTotalBalance();//获取现有总余额
            totalAvailableAmount = channelWallet.getTotalAvailableAmount() == null ? new BigDecimal(0) : channelWallet.getTotalAvailableAmount();  //获取现有总可用余额
            totalAmount = totalAmount.add(investAmount);
            incomeAmount = incomeAmount.add(investAmount);
            totalBalance = totalBalance.add(investAmount);
            totalAvailableAmount = totalAvailableAmount.add(investAmount);
            channelWallet.setTotalAmount(totalAmount);
            channelWallet.setIncomeAmount(incomeAmount);
            channelWallet.setTotalBalance(totalBalance);
            channelWallet.setTotalAvailableAmount(totalAvailableAmount);
            channelWallet.setUpdateTime(new Date());
            i = channelWalletMapper.updateByPrimaryKeySelective(channelWallet);
        }else {
            ChannelWallet channelWallet1 = new ChannelWallet();
            channelWallet1.setId(UUID.createKey("channel_wallet")+"");
            channelWallet1.setChannelId(String.valueOf(map.get("channelId")));
            channelWallet1.setTotalAmount(investAmount);
            channelWallet1.setIncomeAmount(investAmount);
            totalBalance=investAmount;
            channelWallet1.setTotalBalance(investAmount);
            channelWallet1.setTotalAvailableAmount(investAmount);
            channelWallet1.setUpdateTime(new Date());
            i = channelWalletMapper.insertSelective(channelWallet1);
        }
        ChannelDetails channelDetails = new ChannelDetails();
        channelDetails.setChannelId(String.valueOf(map.get("channelId")));
        channelDetails.setId(UUID.createKey("channel_details")+"");
        channelDetails.setOrderId(UUID.createKey("channel_details")+"");
        channelDetails.setType("8");
        channelDetails.setAmount(investAmount);
        channelDetails.setInAmount(investAmount);
        channelDetails.setTotalBalance(totalBalance);
        channelDetails.setUpdateTime(new Date());
        channelDetails.setTimestamp(String.valueOf(new Date().getTime()));
        channelDetailsMapper.insertSelective(channelDetails);
        return i;
    }

    @Override
    public Result deleteByPrimaryKey(List<String> merIds) {
        int i=0;
        Result<String> result = new Result<>();
        for (String merId : merIds) {
            merchantWalletMapper.deleteByPrimaryKey(merId);
            i++;
        }
        if(i==merIds.size()){
            result.setCode(Result.SUCCESS);
            result.setMsg("删除商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("删除商户失败");
        }
        return result;
    }

    @Override
    @Transactional
    public int updateByPrimaryKeySelective(MerchantWallet record){
        return  merchantWalletMapper.updateByPrimaryKeySelective(record);
    }
}
