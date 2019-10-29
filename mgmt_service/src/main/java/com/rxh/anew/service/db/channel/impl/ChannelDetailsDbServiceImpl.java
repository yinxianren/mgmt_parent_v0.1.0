package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ChannelDetailsDbService;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.mapper.anew.channel.AnewChannelDetailsMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:53
 * Description:
 */
@Service
public class ChannelDetailsDbServiceImpl extends ServiceImpl<AnewChannelDetailsMapper, ChannelDetailsTable>implements ChannelDetailsDbService {
}
