package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.BankCodeDbService;
import com.rxh.anew.table.system.BankCodeTable;
import com.rxh.mapper.anew.system.AnewBankCodeMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:51
 * Description:
 */
@Service
public class BankCodeDbServiceImpl extends ServiceImpl<AnewBankCodeMapper, BankCodeTable> implements BankCodeDbService {
}
