package com.wzy.demo.service;

import com.wzy.demo.entity.Balance;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
public interface BalanceService extends IService<Balance> {
    boolean add(Integer userId, BigDecimal gold);
    Balance get(Integer userId);

    boolean reduce(Integer userId, BigDecimal gold);
}
