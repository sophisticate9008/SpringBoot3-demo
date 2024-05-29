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
@TableName("role_permission")
@Schema(name = "RolePermission", description = "")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("role_id")
    private Integer roleId;

    @TableId("permission_id")
    private Integer permissionId;
}
