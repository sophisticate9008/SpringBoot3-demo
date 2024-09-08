package com.wzy.demo.service;

import com.wzy.demo.entity.Message;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzy
 * @since 2024-08-15
 */
public interface MessageService extends IService<Message> {
    List<Message> getListByUser(Integer senderId, Integer receiverId);

    Map<Integer, List<Message>> getAllList(Integer userId);

    void readMessage(Integer senderId, Integer receiverId);
}
