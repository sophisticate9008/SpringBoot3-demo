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
    boolean canceled(String account, Integer id);
    boolean add(Reply reply);
}
