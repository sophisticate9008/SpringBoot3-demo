package com.wzy.demo.common;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class ResultObj {
    
    private Integer code;
    private String msg;
    public static final ResultObj LOGIN_SUCCESS= new ResultObj(Constast.SUCCESS,"登陆成功");
    public static final ResultObj LOGIN_ERROR_PASS= new ResultObj(Constast.ERROR,"用户名或密码错误");
    public static final ResultObj LOGIN_ERROR_CODE= new ResultObj(Constast.ERROR,"验证码错误");
    public static final ResultObj REGISTER_REPEAT = new ResultObj(Constast.ERROR, "用户名已被注册");
    public static final ResultObj REGISTER_SUCCESS = new ResultObj(Constast.SUCCESS, "注册成功");
    public static final ResultObj REGISTER_ERROR = new ResultObj(Constast.ERROR, "注册失败");
}
