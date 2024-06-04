package com.wzy.demo.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(name = "UserVo", description = "登陆注册用")
public class UserVo{
    @Schema(description = "账户")
    String account;

    @Schema(description = "密码")
    String password;

    @Schema(description = "验证码")
    String code;

}
