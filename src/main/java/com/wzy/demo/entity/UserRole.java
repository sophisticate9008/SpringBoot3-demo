package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzy
 * @since 2024-05-29
 */
@Getter
@Setter
@TableName("user_role")
@Schema(name = "UserRole", description = "")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("user_id")
    private Integer userId;

    @TableId("role_id")
    private Integer roleId;
}
