package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ChannelHistoryDbService;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.mapper.anew.channel.AnewChannelHistoryMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 上午9:57
 * Description:
 */
@Service
public class ChannelHistoryDbServiceImpl extends ServiceImpl<AnewChannelHistoryMapper, ChannelHistoryTable> implements ChannelHistoryDbService {
}
