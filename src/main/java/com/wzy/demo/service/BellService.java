package com.wzy.demo.service;

import com.wzy.demo.entity.Bell;

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
public interface BellService extends IService<Bell> {
    public List<Bell> getByUserId(Integer userId);
    public boolean add(Integer userId, String content);

}
