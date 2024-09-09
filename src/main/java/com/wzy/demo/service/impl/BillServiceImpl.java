package com.wzy.demo.service.impl;

import com.wzy.demo.common.MyException;
import com.wzy.demo.entity.Bill;
import com.wzy.demo.mapper.BillMapper;
import com.wzy.demo.service.BalanceService;
import com.wzy.demo.service.BellService;
import com.wzy.demo.service.BillService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private BalanceService balanceService;
    @Override
    @Transactional
    public boolean add(Integer userId, String content, BigDecimal gold) {
        Bill bill = new Bill();
        bill.setUserId(userId).setContent(content).setGold(gold).setType(1);
        balanceService.add(userId, gold);
        return MyException.throwRuntimeException(this.save(bill), "账单添加失败");
    }


    @Override
    public List<Bill> getByUserId(Integer UserId) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", UserId);
        queryWrapper.orderByDesc("created_at");
        return this.list(queryWrapper);
    }


    @Override
    @Transactional
    public boolean reduce(Integer userId, String content, BigDecimal gold) {
        Bill bill = new Bill();
        bill.setUserId(userId).setContent(content).setGold(gold).setType(-1);
        balanceService.reduce(userId, gold);
        return MyException.throwRuntimeException(this.save(bill), "账单添加失败");
    }

    
}
