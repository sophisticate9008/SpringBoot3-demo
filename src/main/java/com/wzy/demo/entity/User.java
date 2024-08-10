package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzy
 * @since 2024-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
@Schema(name = "User", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Hidden
    private Integer id;

    @Schema(name = "account", description = "账号", example = "user1", required = true)
    @TableField("account")
    private String account;

    @Schema(name = "name", description = "姓名", example = "张三")
    @TableField("name")
    private String name;

    @Schema(name = "password", description = "密码", example = "123456", required = true)
    @Hidden
    @TableField("password")
    private String password;

    @Schema(name = "salt", description = "密码加密自动生成")
    @Hidden
    @TableField("salt")
    private String salt;

    @Schema(name = "sex", description = "性别", example = "1")
    @TableField("sex")
    private Integer sex;

    @Schema(name = "age", description = "年龄", example = "18")
    @TableField("age")
    private Integer age;

    @Schema(name = "avatar_path", description = "头像路径")
    @Hidden
    @TableField("avatar_path")
    private String avatarPath;

    @Schema(name = "email", description = "邮箱", example = "123456@qq.com")
    @TableField("email")
    private String email;

    @Schema(name = "phone", description = "电话", example = "12345678901")
    @TableField("phone")
    private String phone;

    @Schema(name = "theType", description = "类型", example = "1", required = true)
    @Hidden
    @TableField("the_type")
    private Integer theType = 1;

    @Schema(name = "state", description = "状态", example = "1")
    @Hidden
    @TableField("state")
    private Integer state = 1;

    @Schema(name = "signature", description = "签名", example = "我是一个签名")
    @TableField("signature")
    private String signature;
}
