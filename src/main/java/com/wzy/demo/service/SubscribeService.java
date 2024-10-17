package com.wzy.demo.service;

import com.wzy.demo.entity.Subscribe;

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
public interface SubscribeService extends IService<Subscribe> {
    public boolean add(Integer userId,Integer commissionId);
    public List<Subscribe> getByUserId(Integer userId);
    public List<Subscribe> getByCommissionId(Integer commissionId);
    public boolean isSubscribed(Integer userId,Integer commissionId);
    public boolean removeByCommissionIds(Integer userId,List<Integer> commissionIds);

}
