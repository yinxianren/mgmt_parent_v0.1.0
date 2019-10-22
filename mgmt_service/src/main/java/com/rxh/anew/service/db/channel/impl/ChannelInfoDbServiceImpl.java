package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ChannelInfoDbService;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.mapper.anew.channel.AnewChannelInfoMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午3:47
 * Description:
 */
@Service
public class ChannelInfoDbServiceImpl extends ServiceImpl<AnewChannelInfoMapper, ChannelInfoTable> implements ChannelInfoDbService {
}
