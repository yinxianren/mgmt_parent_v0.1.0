package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.SystemOrderTrackDbService;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.mapper.anew.system.AnewSystemOrderTrackMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午2:04
 * Description:
 */
@Service
public class SystemOrderTrackDbServiceImpl extends ServiceImpl<AnewSystemOrderTrackMapper, SystemOrderTrackTable> implements SystemOrderTrackDbService {
}
