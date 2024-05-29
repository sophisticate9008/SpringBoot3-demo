package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("user")
@Schema(name = "User", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "account", description = "账号", example = "user1", required = true)
    @TableField("account")
    private String account;

    @Schema(name = "name", description = "姓名", example = "张三")
    @TableField("name")
    private String name;

    @Schema(name = "password", description = "密码", example = "123456", required = true)
    @TableField("password")
    private String password;

    @Schema(name = "salt", description = "密码加密自动生成")
    @TableField("salt")
    private String salt;

    @Schema(name = "sex", description = "性别", example = "1")
    @TableField("sex")
    private Integer sex;

    @Schema(name = "age", description = "年龄", example = "18")
    @TableField("age")
    private Integer age;

    @Schema(name = "theType", description = "类型", example = "1", required = true)
    @TableField("the_type")
    private Integer theType;
}
