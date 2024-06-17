package com.wzy.demo.controller;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        if(user.getAvatarPath() != null && activUser.getAvatarPath() != null && !user.getAvatarPath().equals(activUser.getAvatarPath())) {
            AppFileUtils.removeFileByPath(activUser.getAvatarPath());
            user.setAvatarPath(AppFileUtils.renameFile(user.getAvatarPath()));
        }
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
    
    @PostMapping("basicInfos")
    @Operation(summary = "获取用户基本信息", description = "获取用户基本信息,传入Accounts")
    public DataGridView basicInfos(@RequestBody MultiGetVo multiGetVo) {
        ArrayList<User> users = new ArrayList<>();
        for (String account : multiGetVo.getAccounts()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account);
            User user = userService.getOne(queryWrapper);
            user.setPassword(null).setSalt(null);
            users.add(user);
        }
        return new DataGridView(users);
    }
    
}
