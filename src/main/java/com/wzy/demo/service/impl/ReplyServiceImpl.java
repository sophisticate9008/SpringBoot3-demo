package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.mapper.ReplyMapper;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.ReplyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Autowired
    private CommissionService commissionService;

    @Override
    public boolean canceled(String account, Integer commissionId) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).eq("commission_id", commissionId).eq("state", -2);
        if (this.list(queryWrapper).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add(Reply reply) {
        if (this.canceled(reply.getAccount(), reply.getCommissionId())) {
            return false;
        } else {
            Commission commission = commissionService.getById(reply.getCommissionId());
            if (commission.getState() == 0  && commission.getEndTime().isAfter(LocalDateTime.now())) {
                if (commissionService.getAndSetCommissionNum(reply.getCommissionId()) < commission.getNum()) {
                    reply.setState(0);
                    if(this.save(reply)) {
                        commissionService.getAndSetCommissionNum(reply.getCommissionId());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    @Override
    public boolean unlock(Integer replyId) {
        return setState(replyId, -2);
    }

    @Override
    public boolean apply(Integer replyId) {
        return setState(replyId, 1);
    }

    @Override
    public boolean reject(Integer replyId) {
        return setState(replyId, 2);
    }

    public boolean setState(Integer replyId, Integer state) {
        Reply reply = this.getById(replyId);
        reply.setState(state);
        if(state == -2) {
            reply.setContent("取消锁定");
        }
        return this.updateById(reply);
    }
}
