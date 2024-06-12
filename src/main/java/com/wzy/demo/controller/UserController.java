package com.wzy.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.PasswordUtils;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.UserService;

import cn.hutool.db.Session;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-05-28
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "APIs related to User entity")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("info")
    @Operation(summary = "获取当前登陆用户信息", description = "获取当前登陆用户信息")
    public DataGridView info() {
        User user = (User) WebUtils.getSession().getAttribute("user");
        user = userService.getById(user.getId());
        user.setPassword(null).setSalt(null);
        return new DataGridView(user);
    }

    @PostMapping("/register")
    public ResultObj Register(String account, String password) {
        if(userService.AccountHasRegister(account)) {
            return ResultObj.REGISTER_REPEAT;
        }else {
            return userService.Register(account, password);
        }
    }
    
    @PostMapping("/changePassword")
    public ResultObj changePassword(String oldPassword, String newPassword) {
        User user = (User) WebUtils.getSession().getAttribute("user");
        if(PasswordUtils.hashPassword(oldPassword, user.getSalt()).equals(user.getPassword())) {
            user.setPassword(PasswordUtils.hashPassword(newPassword, user.getSalt()));
            userService.updateById(user);
            return ResultObj.UPDATE_SUCCESS;
        }else {
            return ResultObj.UPDATE_ERROR;
        }
    }
    @PostMapping("/changeProfile")
    public ResultObj changeProfile(@RequestBody User user) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        user.setSalt(null).setTheType(null).setPassword(null);
        if(user.getAccount().equals(activUser.getAccount())) {
            user.setId(activUser.getId());
            if(userService.updateById(user)) {
                return ResultObj.UPDATE_SUCCESS;
            }else {
                return ResultObj.UPDATE_ERROR;
            }
        }else {
            return ResultObj.Permission_Exceed;
        }
    }
    
    
}
