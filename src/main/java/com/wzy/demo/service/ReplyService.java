package com.wzy.demo.service;

import com.wzy.demo.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
public interface ReplyService extends IService<Reply> {
    boolean canceled(String account, Integer replyId);
    boolean add(Reply reply);

    boolean unlock(Integer replyId);

    boolean apply(Integer replyId);

    boolean reject(Integer replyId);
}
