package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.BankRateDbService;
import com.rxh.anew.table.system.BankRateTable;
import com.rxh.mapper.anew.system.AnewBankRateMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:51
 * Description:
 */
@Service
public class BankRateDbServiceImpl extends ServiceImpl<AnewBankRateMapper, BankRateTable> implements BankRateDbService {
}
