package com.rxh.service.impl;

import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.mapper.square.ChannelInfoMapper;
import com.rxh.mapper.square.OrganizationInfoMapper;
import com.rxh.mapper.sys.SysConstantMapper;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {


    @Resource
    private OrganizationInfoMapper organizationInfoMapper;
    @Resource
    private ChannelInfoMapper channelInfoMapper;
    @Resource
    private SysConstantMapper sysConstantMapper;
    @Autowired
    private ApiProductTypeSettingService apiProductTypeSettingService;

    @RedisCacheDeleteByHashKey(hashKey = "organization_info")
    @Override
    public int insert(OrganizationInfo record) {

        String idstr=organizationInfoMapper.selectLastId()==null?null:organizationInfoMapper.selectLastId();
        String id = UUID.createNumber("ORG",idstr);
        record.setOrganizationId(id);
        record.setCreateTime(new Date());
        int i = organizationInfoMapper.insertSelective(record);
        return i;
    }




    @RedisCacheDeleteByHashKey(hashKey = "organization_info")
    @Override
    public int deleteByPrimaryKey( List<String> idList) {
        for (String id : idList) {
            Integer num=channelInfoMapper.countByOrgId(id);
            if (num>0){
                return 0;
            }
        }
        int i=0;
        for (String integer : idList) {

            organizationInfoMapper.deleteByPrimaryKey(integer);
            i++;
        }

        return i;
    }

    @RedisCacheDeleteByHashKey(hashKey = "organization_info")
    @Override
    public int updateByPrimaryKeySelective( OrganizationInfo record) {
        int updateSuccess = organizationInfoMapper.updateByPrimaryKeySelective(record);
        ProductSettingTable table = new ProductSettingTable();
        table.setOrganizationId(record.getOrganizationId());
        List<ProductSettingTable> list = apiProductTypeSettingService.list(table);
        List<String> ids = Arrays.asList(record.getProductIds().split(","));
        for (ProductSettingTable productSettingTable : list){
            if (ids.contains(productSettingTable.getProductId())){
                if (productSettingTable.getStatus() == 1)
                    continue;
                productSettingTable.setStatus(1);
            }else {
                productSettingTable.setStatus(0);
            }
        }
        Boolean b = apiProductTypeSettingService.batchUpdate(list);
        if (b) updateSuccess+=1;
        return updateSuccess;
    }



    @Override
    public List<OrganizationInfo> getAll(OrganizationInfo record) {
        String remark = record.getRemark();
        String organizationName = record.getOrganizationName();
        Date createTime = record.getCreateTime();
        String creator = record.getCreator();
        String organizationId = record.getOrganizationId();
        if(remark==null&&organizationName==null&&createTime==null&&creator==null&&organizationId==null){

            return organizationInfoMapper.getAll();
        }
        else {
            return   organizationInfoMapper.selectByObj(record);
        }
    }

    @Override
    public List<OrganizationInfo> getIdsAndName() {
        return organizationInfoMapper.getgetIdsAndName();
    }



    @Override
    public List<OrganizationInfo> selectAll() {
        return organizationInfoMapper.selectByExample(null);
    }
}
