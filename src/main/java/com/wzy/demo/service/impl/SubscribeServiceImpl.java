package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Subscribe;
import com.wzy.demo.mapper.SubscribeMapper;
import com.wzy.demo.service.SubscribeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

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
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, Subscribe> implements SubscribeService {

    @Override
    public boolean add(Integer userId, Integer commissionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public boolean remove(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public List<Subscribe> getByUserId(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByUserId'");
    }

    @Override
    public List<Subscribe> getByCommissionId(Integer commissionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByCommissionId'");
    }

}
