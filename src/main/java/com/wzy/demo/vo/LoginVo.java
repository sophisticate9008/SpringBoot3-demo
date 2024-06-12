package com.wzy.demo.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(name = "LoginVo", description = "登陆注册用")
public class LoginVo{
    
    @Schema(description = "账户")
    String account;

    @Schema(description = "密码")
    String password;

    @Schema(description = "验证码")
    String code;

}
