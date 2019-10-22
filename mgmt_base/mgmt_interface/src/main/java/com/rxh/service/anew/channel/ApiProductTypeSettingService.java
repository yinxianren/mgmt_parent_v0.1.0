package com.rxh.service.anew.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.table.channel.ProductSettingTable;

import java.util.List;

public interface ApiProductTypeSettingService {

    public Boolean SaveOrUpdate(ProductSettingTable productSettingTable);

    public Boolean removeById(String id);

    public List<ProductSettingTable> list(LambdaQueryWrapper queryWrapper);
}
