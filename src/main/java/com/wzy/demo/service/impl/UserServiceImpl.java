package com.wzy.demo.service.impl;

import com.wzy.demo.common.PasswordUtils;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;
import com.wzy.demo.mapper.UserMapper;
import com.wzy.demo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-05-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Boolean AccountHasRegister(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long count = this.baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public ResultObj Register(String account, String password) {
        String salt = PasswordUtils.generateRandomSalt();
        String hashPassword = PasswordUtils.hashPassword(password,salt);
        User user = new User();
        user.setAccount(account).setPassword(hashPassword).setSalt(salt);
        if (this.save(user)) {
            return ResultObj.REGISTER_SUCCESS;
        }else {
            return ResultObj.REGISTER_ERROR;
        }
    }

}
