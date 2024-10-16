package com.wzy.demo.service;

import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-05-28
 */
public interface UserService extends IService<User> {
    public Boolean AccountHasRegister(String account);
    public ResultObj Register(String account, String password);
    

}
