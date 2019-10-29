package com.rxh.anew.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.channel.ChannelWalletDbService;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.mapper.anew.channel.AnewChannelWalletMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:53
 * Description:
 */
@Service
public class ChannelWalletDbServiceImpl extends ServiceImpl<AnewChannelWalletMapper, ChannelWalletTable> implements ChannelWalletDbService {
}
