package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.mapper.ReplyMapper;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.ReplyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    public boolean canceled(String account, Integer id) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).eq("commission_id", id).eq("state", -2);
        if(this.getOne(queryWrapper) != null) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean add(Reply reply) {
        if(this.canceled(reply.getAccount(), reply.getCommissionId())){
            return false;
        }else {
            Commission commission = commissionService.getById(reply.getCommissionId());
            if(commission.getState() == 0){
                reply.setState(0);
                if(this.save(reply)) {
                    if(commissionService.getCommissionNum(reply.getCommissionId()) >= commission.getNum()) {
                        commission.setState(2);
                        commissionService.updateById(commission);
                    }
                    return true;
                }
                return false;
            }else {
                return false;
            }
        }
    }

}
