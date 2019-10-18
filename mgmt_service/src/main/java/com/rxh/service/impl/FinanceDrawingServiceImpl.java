package com.rxh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxh.mapper.square.*;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.square.FinanceDrawingService;
import com.rxh.service.sys.SysConstantService;
import com.rxh.square.pojo.*;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Handler;


@Service
public class FinanceDrawingServiceImpl implements FinanceDrawingService {

    @Resource
    private FinanceDrawingSquareMapper financeDrawingSquareMapper;

    @Resource
    private MerchantWalletMapper merchantWalletMapper;

    @Resource
    private AgentWalletMapper agentWalletMapper;
    @Resource
    private MerchantsDetailsMapper merchantsDetailsMapper;
    @Resource
    private AgentMerchantsDetailsMapper agentMerchantsDetailsMapper;
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ConstantService constantService;

    @Override
    public List<FinanceDrawing> search(FinanceDrawing financeDrawing) {
        return financeDrawingSquareMapper.search(financeDrawing.getCustomerId());
    }

    @Override
    public PageResult findByPage(Page page) {
        // 设置分页信息
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        if (page.getOrderBy() == null || page.getOrderBy().size() == 0) {
            PageHelper.orderBy("application_time DESC");
        } else {
            page.getOrderBy().forEach((key, value) -> PageHelper.orderBy(key + " " + value));
        }
        // 执行查询
        if (page.getSearchInfo() != null && page.getSearchInfo().getEndDate() != null) {
            page.getSearchInfo().setEndDate(com.rxh.utils.DateUtils.addDays(page.getSearchInfo().getEndDate(), 1));
        }
        // 执行查询
        if (page.getSearchInfo() != null && page.getSearchInfo().getEndDate2() != null) {
            page.getSearchInfo().setEndDate2(com.rxh.utils.DateUtils.addDays(page.getSearchInfo().getEndDate2(), 1));
        }
        List<FinanceDrawing> list = financeDrawingSquareMapper.selectBySearchInfo(page.getSearchInfo());
        PageInfo<FinanceDrawing> pageInfo = new PageInfo<>(list);
        // 获取查询结果
        PageResult result = new PageResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        result.setAllPage(pageInfo.getPages());
        // 返回结果
        return result;
    }

    @Override
    public Map<String, Object> getBalanceChangeInit() {
        Map<String, Object> init = new HashMap<>();
        List<SysConstant> status = constantService.getConstantByGroupName(SystemConstant.DRAWSTATUS);
        List<Customer> list=new ArrayList<>();
        List<Customer> agents = customerMapper.getAgents();
        list.addAll(agents);
        List<Customer> merChants = customerMapper.getMerChants();
        list.addAll(merChants);
        init.put("status", status);
        init.put("customers",list);
        return init;
    }

    @Override
    public Boolean update(FinanceDrawing param) {
        //若提现申请驳回 余额更新

        if (param.getStatus().equals(SystemConstant.DRAW_NO_SUCCESS)) {
            BigDecimal amount = param.getDrawingAmount();
            String customerId = param.getCustomerId();
            if (customerId.startsWith("A")) {
                AgentWallet agentWallet = agentWalletMapper.selectByPrimaryKey(customerId);
                agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount().add(amount));
//                agentWallet.setOutAmount(agentWallet.getOutAmount().subtract(amount));
                agentWalletMapper.updateByPrimaryKey(agentWallet);
            }
            if (customerId.startsWith("M")) {
                MerchantWallet merchantWallet = merchantWalletMapper.selectByPrimaryKey(customerId);
                merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().add(amount));
//                merchantWallet.setOutAmount(merchantWallet.getOutAmount().subtract(amount));
                merchantWalletMapper.updateByPrimaryKey(merchantWallet);
            }
        }
        return financeDrawingSquareMapper.updateByPrimaryKey(param) > 0;
    }

    @Override
    public Boolean drawMoney(FinanceDrawing param) {
        BigDecimal amount = param.getDrawingAmount();
        String customerId = param.getCustomerId();
        param.setStatus(SystemConstant.DRAW_PAY_SUCCESS);
        param.setTransferTime(new Date());
        boolean flag = financeDrawingSquareMapper.updateByPrimaryKey(param) > 0;
        if (customerId.startsWith("A")) {
            AgentWallet agentWallet = agentWalletMapper.selectByPrimaryKey(customerId);
            agentWallet.setTotalBalance(agentWallet.getTotalBalance().subtract(amount));
            agentWallet.setOutAmount(agentWallet.getOutAmount()==null?amount:agentWallet.getOutAmount().add(amount));
            agentWalletMapper.updateByPrimaryKey(agentWallet);
            AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
            agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
            agentMerchantsDetails.setAgentMerId(customerId);
            agentMerchantsDetails.setOrderId(param.getId());
            agentMerchantsDetails.setAmount(amount);
            agentMerchantsDetails.setType(SystemConstant.DARW_MONEY);
            agentMerchantsDetails.setOutAmount(amount);
            agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
            agentMerchantsDetails.setUpdateTime(new Date());
            agentMerchantsDetailsMapper.insertSelective(agentMerchantsDetails);
        }
        if (customerId.startsWith("M")) {
            MerchantWallet merchantWallet = merchantWalletMapper.selectByPrimaryKey(customerId);
            merchantWallet.setTotalBalance(merchantWallet.getTotalBalance().subtract(amount));
            merchantWallet.setOutAmount(merchantWallet.getOutAmount()==null?amount:merchantWallet.getOutAmount().add(amount));
            merchantWalletMapper.updateByPrimaryKey(merchantWallet);
            MerchantsDetails merchantsDetails = new MerchantsDetails();
            merchantsDetails.setId(UUID.createKey("merchants_details", ""));
            merchantsDetails.setMerId(customerId);
            merchantsDetails.setType(SystemConstant.DARW_MONEY);
            merchantsDetails.setOrderId(param.getId());
            merchantsDetails.setAmount(amount);
            merchantsDetails.setOutAmount(amount);
            merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
            merchantsDetails.setUpdateTime(new Date());
            merchantsDetailsMapper.insertSelective(merchantsDetails);
        }
        return flag;
    }
}
