package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.mapper.CommissionMapper;
import com.wzy.demo.mapper.ReplyMapper;
import com.wzy.demo.service.CommissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public boolean locked(Integer id) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("commission_id", id);
        replyQueryWrapper.in("state", -1, 0, 2);
        Long count = replyMapper.selectCount(replyQueryWrapper);
        return count > 0;
    }

    @Override
    public Integer getCommissionNum(Integer id) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("commission_id", id);
        replyQueryWrapper.ne("state", 2);
        long count = replyMapper.selectCount(replyQueryWrapper);
        return (int) count;
    }
}
