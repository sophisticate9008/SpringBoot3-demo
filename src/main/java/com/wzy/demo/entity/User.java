package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzy
 * @since 2024-05-27
 */
@Getter
@Setter
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("account")
    private String account;

    @TableField("nickname")
    private String nickname;

    @TableField("password")
    private String password;

    @TableField("address")
    private String address;

    @TableField("sex")
    private String sex;

    @TableField("merchant")
    private String merchant;

    @TableField("type")
    private Integer type;

    @TableField("avatarpath")
    private String avatarpath;

    @TableField("salt")
    private String salt;

    @TableField("available")
    private Integer available;

    @TableField("gold")
    private BigDecimal gold;
}
