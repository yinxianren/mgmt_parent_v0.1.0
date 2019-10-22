package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ChannelExtraInfoDbService;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.mapper.anew.channel.AnewChannelExtraInfoMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午10:18
 * Description:
 */
@Service
public class ChannelExtraInfoDbServiceImpl extends ServiceImpl<AnewChannelExtraInfoMapper, ChannelExtraInfoTable> implements ChannelExtraInfoDbService {
}
