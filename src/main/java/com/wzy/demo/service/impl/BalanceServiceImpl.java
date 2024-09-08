package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Balance;
import com.wzy.demo.mapper.BalanceMapper;
import com.wzy.demo.service.BalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {

    @Override
    public boolean add(Integer userId, BigDecimal gold) {
        Balance balance = this.get(userId);
        balance.setGold(balance.getGold().add(gold));
        return this.updateById(balance);
    }
    
    @Override
    public Balance get(Integer userId) {
        Balance balance = this.getById(userId);
        if(balance == null) {
            balance = new Balance();
            balance.setUserId(userId);
            this.save(balance);
            return balance;
        }else {
            return balance;
        }
    }

    @Override
    public boolean reduce(Integer userId, BigDecimal gold) {
        Balance balance = this.get(userId);
        balance.setGold(balance.getGold().subtract(gold));
        return this.updateById(balance);
    }

}
