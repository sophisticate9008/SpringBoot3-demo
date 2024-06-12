package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("reply")
@Schema(name = "Reply", description = "")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Hidden
    private Integer id;

    @Schema(name = "content", description = "回答内容(文本或路径名)")
    @TableField("content")
    private String content;

    @Schema(name = "account", description = "回答者账号")
    @TableField("account")
    private String account;

    @Schema(name = "commissionId", description = "回答所属的委托id")
    @TableField("commission_id")
    private Integer commissionId;

    @Schema(name = "replyTime", description = "回答时间")
    @TableField("reply_time")
    private LocalDateTime replyTime;

    @Schema(name = "state", description = "回答状态(-2: 取消锁定, -1:锁定委托中,0:未审核,1:审核通过,2:审核不通过)")
    @TableField("state")
    @Hidden
    private Integer state;
}
