package com.wzy.demo.controller;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.common.ActiverUser;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;
import com.wzy.demo.vo.LoginVo;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/login")
public class LoginController {
    @PostMapping("login")
    @Operation(summary = "登陆", description = "登陆")
    public ResultObj login(@RequestBody LoginVo userVo, HttpSession session) {
        String sessionCode = (String) session.getAttribute("code");
        String code = userVo.getCode();
        String account = userVo.getAccount();
        String password = userVo.getPassword();
        System.out.println(sessionCode + account);
        if (code != null && sessionCode.equalsIgnoreCase(code)) {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(account, password);
            try {
                // 对用户进行认证登陆
                subject.login(token);
                // 通过subject获取以认证活动的user
                ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
                // 将user存储到session中
                WebUtils.getSession().setAttribute("user", activerUser.getUser());
                return ResultObj.LOGIN_SUCCESS;
            } catch (AuthenticationException e) {
                e.printStackTrace();
                return ResultObj.LOGIN_ERROR_PASS;
            }
        } else {
            return ResultObj.LOGIN_ERROR_CODE;
        }

    }
    // @PostMapping("loginByToken")
    // @Operation(summary = "用token登陆", description = "用token登陆")
    // public ResultObj loginByToken(String token, HttpSession session){
    // Subject subject = SecurityUtils.getSubject();

    // }
    @PostMapping("logout")
    @Operation(summary = "注销", description = "注销")
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @GetMapping("isLogin")
    @Operation(summary = "判断是否登陆", description = "判断是否登陆")
    public ResultObj isLogin() {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        return activUser == null ? ResultObj.LOGIN_ERROR : ResultObj.LOGIN_SUCCESS;
    }

    @RequestMapping("getCode")
    @Operation(summary = "获取验证码", description = "获取验证码")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 5);
        session.setAttribute("code", lineCaptcha.getCode());
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            lineCaptcha.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
