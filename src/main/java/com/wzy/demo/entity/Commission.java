package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
 * @since 2024-06-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("commission")
@Schema(name = "Commission", description = "")
public class Commission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Hidden
    private Integer id;

    @Schema(name = "account", description = "委托者账号")
    @TableField("account")
    private String account;

    @Schema(name = "name", description = "委托名称")
    @TableField("name")
    private String name;

    @Schema(name = "description", description = "委托描述")
    @TableField("description")
    private String description;

    @Schema(name = "beginTime", description = "委托开始时间")
    @TableField("begin_time")
    private LocalDateTime beginTime;

    @Schema(name = "endTime", description = "委托结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(name = "money", description = "委托金额")
    @TableField("money")
    private BigDecimal money;

    @Schema(name = "num", description = "委托需要结果数量")
    @TableField("num")
    private Integer num;

    @Schema(name = "state", description = "委托状态(-1:委托终止(取消或时间已到), 0:委托进行, 1:委托完成, 2:委托已满)")
    @Hidden
    @TableField("state")
    private Integer state;
}
