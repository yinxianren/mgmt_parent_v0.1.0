package com.rxh.service.anew.channel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rxh.anew.table.channel.ChannelDetailsTable;

public interface ApiChannelDetailsService {

    ChannelDetailsTable  getOne(ChannelDetailsTable cdt);

    boolean updateOrSave(ChannelDetailsTable cdt);

    IPage page(ChannelDetailsTable cdt);
}
