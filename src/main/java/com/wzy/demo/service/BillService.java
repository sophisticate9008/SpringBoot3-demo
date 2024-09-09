package com.wzy.demo.service;

import com.wzy.demo.entity.Bill;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
public interface BillService extends IService<Bill> {
    public boolean add(Integer userId, String content, BigDecimal gold);
    public boolean reduce(Integer userId, String content, BigDecimal gold);
    public List<Bill> getByUserId(Integer UserId);
}
