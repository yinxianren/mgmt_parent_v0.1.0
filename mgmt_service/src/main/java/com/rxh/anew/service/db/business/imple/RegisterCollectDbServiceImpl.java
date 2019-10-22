package com.rxh.anew.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.business.RegisterCollectDbService;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.mapper.anew.business.AnewRegisterCollectMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:40
 * Description:
 */
@Service
public class RegisterCollectDbServiceImpl extends ServiceImpl<AnewRegisterCollectMapper, RegisterCollectTable> implements RegisterCollectDbService {
}
