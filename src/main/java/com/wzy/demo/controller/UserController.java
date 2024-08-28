package com.wzy.demo.controller;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.AppFileUtils;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.PasswordUtils;

import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.UserService;
import com.wzy.demo.vo.MultiGetVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@InjectUser
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "APIs related to User entity")
public class UserController {
    private User activeUser;
    @Autowired
    private UserService userService;
    
    @GetMapping("info")
    @Operation(summary = "获取当前登陆用户信息", description = "获取当前登陆用户信息")
    public DataGridView info() {
       
        User user = userService.getById(activeUser.getId());
        user.setPassword(null).setSalt(null);
        return new DataGridView(user);
    }

    @GetMapping("/register")
    public ResultObj Register(String account, String password) {
        if(userService.AccountHasRegister(account)) {
            return ResultObj.REGISTER_REPEAT;
        }else {
            return userService.Register(account, password);
        }
    }
    
    @PostMapping("/changePassword")
    public ResultObj changePassword(String oldPassword, String newPassword) {
        
        if(PasswordUtils.hashPassword(oldPassword, activeUser.getSalt()).equals(activeUser.getPassword())) {
            activeUser.setPassword(PasswordUtils.hashPassword(newPassword, activeUser.getSalt()));
            userService.updateById(activeUser);
            return ResultObj.UPDATE_SUCCESS;
        }else {
            return ResultObj.UPDATE_ERROR;
        }
    }
    @PostMapping("/changeProfile")
    public ResultObj changeProfile(@RequestBody User user) {

        if(user.getAvatarPath() != null && activeUser.getAvatarPath() != null && !user.getAvatarPath().equals(activeUser.getAvatarPath())) {
            AppFileUtils.removeFileByPath(activeUser.getAvatarPath());
            user.setAvatarPath(AppFileUtils.renameFile(user.getAvatarPath()));
        }
        user.setSalt(null).setTheType(null).setPassword(null);
        if(user.getAccount().equals(activeUser.getAccount())) {
            user.setId(activeUser.getId());
            if(userService.updateById(user)) {
                return ResultObj.UPDATE_SUCCESS;
            }else {
                return ResultObj.UPDATE_ERROR;
            }
        }else {
            return ResultObj.Permission_Exceed;
        }
    }
    
    @PostMapping("basicInfos")
    @Operation(summary = "获取用户基本信息", description = "获取用户基本信息,传入Accounts")
    public DataGridView basicInfos(@RequestBody MultiGetVo multiGetVo) {
        ArrayList<User> users = new ArrayList<>();
        for (String account : multiGetVo.getAccounts()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account);
            User user = userService.getOne(queryWrapper);
            user.setPassword(null).setSalt(null).setPhone(null).setEmail(null);
            users.add(user);
        }
        return new DataGridView(users);
    }
    
}
