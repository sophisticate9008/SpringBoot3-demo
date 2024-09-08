package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Bell;
import com.wzy.demo.mapper.BellMapper;
import com.wzy.demo.service.BellService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class BellServiceImpl extends ServiceImpl<BellMapper, Bell> implements BellService {

    @Autowired
    private BellService bellService;
    @Override
    public List<Bell> getByUserId(Integer userId) {
        QueryWrapper<Bell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.list(queryWrapper);
    }


    @Override
    public boolean add(Integer userId, String content) {
        Bell bell = new Bell();
        bell.setUserId(userId).setContent(content);
        return bellService.save(bell);
    }




    @Override
    public boolean remove(Integer[] ids) {
        return bellService.removeById(ids);
    }

}
