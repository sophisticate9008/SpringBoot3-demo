package com.wzy.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<User> getAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("John Doe");
        User user2 = new User();
        user2.setId(2);
        user2.setName("Jane Doe");
        return Arrays.asList(user1, user2);
    }

    @PostMapping("/register")
    public ResultObj Register(String account, String password) {
        if(userService.AccountHasRegister(account)) {
            return ResultObj.REGISTER_REPEAT;
        }else {
            return userService.Register(account, password);
        }
    }
    
}
