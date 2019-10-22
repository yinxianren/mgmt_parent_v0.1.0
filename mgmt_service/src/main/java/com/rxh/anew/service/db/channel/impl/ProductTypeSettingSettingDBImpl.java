package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ProductTypeSettingDBService;
import com.rxh.anew.table.channel.ProductSettingTable;
import com.rxh.mapper.anew.channel.ProductTypeSettingMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeSettingSettingDBImpl extends ServiceImpl<ProductTypeSettingMapper, ProductSettingTable> implements ProductTypeSettingDBService {
}
