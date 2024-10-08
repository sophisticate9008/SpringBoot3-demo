package com.wzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2024-08-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("message")
@Schema(name = "Message", description = "")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "content", description = "")
    @TableField("content")
    private String content;

    @Schema(name = "senderId", description = "发送者id")
    @TableField("sender_id")
    private Integer senderId;

    @Schema(name = "receiverId", description = "接收者id")
    @TableField("receiver_id")
    private Integer receiverId;

    @Schema(name = "haveRead", description = "")
    @TableField("haveRead")
    private Boolean haveRead;

    @Schema(name = "sendTime", description = "")
    @TableField("send_time")
    private LocalDateTime sendTime;
}
