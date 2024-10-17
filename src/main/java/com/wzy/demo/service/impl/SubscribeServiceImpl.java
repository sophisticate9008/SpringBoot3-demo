package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Subscribe;
import com.wzy.demo.mapper.SubscribeMapper;
import com.wzy.demo.service.SubscribeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@Service
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, Subscribe> implements SubscribeService {

    @Override
    public boolean add(Integer userId, Integer commissionId) {
        if (!isSubscribed(userId, commissionId)) {
            return this.save(new Subscribe().setUserId(userId).setCommissionId(commissionId));
        }
        return false;

    }

    @Override
    public List<Subscribe> getByUserId(Integer userId) {
        return this.lambdaQuery().eq(Subscribe::getUserId, userId).list();
    }

    @Override
    public List<Subscribe> getByCommissionId(Integer commissionId) {
        return this.lambdaQuery().eq(Subscribe::getCommissionId, commissionId).list();
    }

    @Override
    public boolean isSubscribed(Integer userId, Integer commissionId) {
        return this.lambdaQuery().eq(Subscribe::getUserId, userId).eq(Subscribe::getCommissionId, commissionId)
                .count() > 0;
    }

    @Override
    public boolean removeByCommissionIds(Integer userId, List<Integer> commissionIds) {
        QueryWrapper<Subscribe> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .in("commission_id", commissionIds);
        return this.remove(wrapper);
    }

}
