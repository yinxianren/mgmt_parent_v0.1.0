package com.rxh.service.impl;

import com.rxh.mapper.square.ChannelDetailsMapper;
import com.rxh.mapper.square.ChannelInfoMapper;
import com.rxh.mapper.square.ChannelWalletMapper;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.square.pojo.*;
import com.rxh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelWalletServiceImpl implements ChannelWalletService {

    @Autowired
    private ChannelWalletMapper channelWalletMapper;
    @Autowired
    private ChannelInfoMapper channelInfoMapper;
    @Autowired
    private ChannelDetailsMapper channelDetailsMapper;

    @Override
    public List<ChannelWallet> search(ChannelWallet channelWallet) {

        return channelWalletMapper.getWalletByParam(channelWallet);
    }



    @Override
    public List<ChannelInfo> getIdsAndName() {
        return channelInfoMapper.selectAllIdAndName();
    }

    @Override
    public Result deleteByPrimaryKey(List<String> channelIds) {
        int i=0;
        Result<String> result = new Result<>();
        for (String channelId : channelIds) {
            channelWalletMapper.deleteByPrimaryKey(channelId);
            i++;
        }
        if(i==channelIds.size()){
            result.setCode(Result.SUCCESS);
            result.setMsg("删除商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("删除商户失败");
        }
        return result;
    }


    @Override
    public PageResult findChannelWallteDetails(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
//            int startPage = 0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
//            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<ChannelDetails> list;
            int totalCount;
            //totalCount  sql  方法
            totalCount = channelDetailsMapper.selectSuccessCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = channelDetailsMapper.selectAllChannelDetails(paramMap);
            List<ChannelInfo> channelInfoList = channelInfoMapper.selectAllIdAndName();
            Map channelMap = new HashMap();
            for (ChannelInfo channelInfo : channelInfoList){
                channelMap.put(channelInfo.getChannelId(),channelInfo.getChannelName());
            }
            for (ChannelDetails channelDetails : list){
                if (channelMap.get(channelDetails.getChannelId()) != null){
                    channelDetails.setChannelName(channelMap.get(channelDetails.getChannelId()).toString());
                }else {
                    channelDetails.setChannelName(channelDetails.getChannelId());
                }
            }
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
    public  ChannelWallet getChannelWallet(String channelId){
        return channelWalletMapper.getChannelWallet(channelId);

    }
    @Override
    @Transactional
    public int updateByPrimaryKeySelective(ChannelWallet record){

        return channelWalletMapper.updateByPrimaryKeySelective(record);
    }

}
