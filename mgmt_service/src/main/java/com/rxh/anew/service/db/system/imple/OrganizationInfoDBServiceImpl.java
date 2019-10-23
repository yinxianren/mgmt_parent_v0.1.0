package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.OrganizationInfoDBService;
import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.mapper.anew.system.AnewOrganizationInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class OrganizationInfoDBServiceImpl extends ServiceImpl<AnewOrganizationInfoMapper, OrganizationInfoTable> implements OrganizationInfoDBService {
}
