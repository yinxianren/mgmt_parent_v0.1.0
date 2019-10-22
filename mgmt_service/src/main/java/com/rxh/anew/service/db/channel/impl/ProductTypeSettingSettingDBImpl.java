package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ProductTypeSettingDBService;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.mapper.anew.channel.AnewProductTypeSettingMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeSettingSettingDBImpl extends ServiceImpl<AnewProductTypeSettingMapper, ProductSettingTable> implements ProductTypeSettingDBService {
}
