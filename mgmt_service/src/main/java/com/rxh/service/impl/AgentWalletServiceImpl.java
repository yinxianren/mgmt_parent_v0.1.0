package com.rxh.service.impl;

import com.rxh.mapper.square.AgentMerchantInfoMapper;
import com.rxh.mapper.square.AgentMerchantsDetailsMapper;
import com.rxh.mapper.square.AgentWalletMapper;
import com.rxh.mapper.square.FinanceDrawingSquareMapper;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.AgentWalletService;
import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.square.pojo.PayOrder;
import com.rxh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rxh.utils.UUID;
import com.rxh.square.pojo.FinanceDrawing;

import javax.annotation.Resource;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class AgentWalletServiceImpl implements AgentWalletService {

    @Autowired
    private AgentWalletMapper agentWalletMapper;

    @Autowired
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;

    @Autowired
    AgentMerchantInfoMapper agentMerchantInfoMapper;

    @Resource
    private FinanceDrawingSquareMapper financeDrawingSquareMapper;




    @Override
    public List<AgentWallet> search(AgentWallet agentWallet) {
        return agentWalletMapper.search(agentWallet.getAgentMerchantId());
    }

    @Override
    @Transactional
    public boolean insert(AgentWallet agentWallet) {
        AgentWallet agent = agentWalletMapper.selectByPrimaryKey(agentWallet.getAgentMerchantId()); //获取钱包信息
        if(agent!=null){
            BigDecimal amount = agent.getTotalAvailableAmount();
          if(amount.compareTo(new BigDecimal(0))>0){
              FinanceDrawing financeDrawing = new FinanceDrawing();
              financeDrawing.setId(UUID.createDrawalNumber(financeDrawingSquareMapper.getMaxDrawingId()));
               financeDrawing.setCustomerId(agentWallet.getAgentMerchantId());
                financeDrawing.setDrawingAmount(agentWallet.getWithdrawalAmount());
               financeDrawing.setStatus(0);
               financeDrawing.setApplicationTime(new Date());
               financeDrawing.setApplicant(agentMerchantInfoMapper.selectByPrimaryKey(agentWallet.getAgentMerchantId()).getAgentMerchantName());
              int finance = financeDrawingSquareMapper.insertSelective(financeDrawing);
                if (finance != 0){

                   AgentWallet wallet =new AgentWallet();
                   BigDecimal availableAmount = agentWallet.getTotalAvailableAmount().subtract(agentWallet.getWithdrawalAmount());
                    wallet.setAgentMerchantId(agentWallet.getAgentMerchantId());
                   wallet.setTotalAvailableAmount(availableAmount);

 //                  wallet.setWithdrawalAmount(agentWallet.getWithdrawalAmount());
                 int agn = agentWalletMapper.updateByPrimaryKeySelective(wallet);
                  if (agn != 0) {
                       return true;
                  }
               }
            }
        }
        return false;
    }

    @Override
    public Result deleteByPrimaryKey(List<String> agentMerchantIds) {
        int i=0;
        Result<String> result = new Result<>();
        for (String agentMerchantId : agentMerchantIds) {
            agentWalletMapper.deleteByPrimaryKey(agentMerchantId);
            i++;
        }
        if(i==agentMerchantIds.size()){
            result.setCode(Result.SUCCESS);
            result.setMsg("删除商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("删除商户失败");
        }
        return result;
    }

    @Override
    public PageResult findAgentWallteDetails(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
//            int startPage = 0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
//            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
//            paramMap.put("agentMerId",paramMap.get("agentMerId"));
            List<AgentMerchantsDetails> list;
            int totalCount;
            //totalCount  sql  方法
            totalCount = agentMerchantsDetailsMapper.selectSuccessCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = agentMerchantsDetailsMapper.selectAllAgentMerchantsDetails(paramMap);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            // 返回结果
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
@Override
    public  AgentWallet selectByPrimaryKey(String agentMerchantId){
        return  agentWalletMapper.selectByPrimaryKey(agentMerchantId);
}
@Override
@Transactional
public int updateByPrimaryKeySelective(AgentWallet record){
    return agentWalletMapper.updateByPrimaryKeySelective(record);
}

}
