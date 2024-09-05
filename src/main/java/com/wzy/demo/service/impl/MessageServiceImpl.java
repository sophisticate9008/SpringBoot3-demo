package com.wzy.demo.service.impl;

import com.wzy.demo.entity.Message;
import com.wzy.demo.mapper.MessageMapper;
import com.wzy.demo.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-08-15
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> getListByUser(String sender, String receiver) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender", sender).eq("receiver", receiver).or().eq("sender", receiver).eq("receiver", sender);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, List<Message>> getAllList(String sender) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender", sender).or().eq("receiver", sender);
        List<Message> messages = this.baseMapper.selectList(queryWrapper);
        Map<String, List<Message>> groupedMessages = messages.stream()
                .collect(Collectors.groupingBy(message -> {
                    // 分组的 key 是对方的标识，即对方的用户名
                    if (message.getSender().equals(sender)) {
                        return message.getReceiver();
                    } else {
                        return message.getSender();
                    }
                }));
        return groupedMessages.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    // 获取每组最后一条消息的时间
                    Message lastMessage1 = entry1.getValue().get(entry1.getValue().size() - 1);
                    Message lastMessage2 = entry2.getValue().get(entry2.getValue().size() - 1);
                    return lastMessage2.getSendTime().compareTo(lastMessage1.getSendTime());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // merge function
                        LinkedHashMap::new // 保持排序
                ));
    }

    @Override
    public void readMessage(String sender, String receiver) {
        // 创建查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender", sender)
                .eq("receiver", receiver)
                .orderByDesc("id").last("LIMIT 1");
        ;

        // 查询最新的消息
        Message theMsg = this.baseMapper.selectOne(queryWrapper);

        // 如果消息存在，更新其状态
        if (theMsg != null) {
            theMsg.setHaveRead(true);
            this.baseMapper.updateById(theMsg); // 使用 updateById 进行更新
        } else {
            // 处理未找到消息的情况（可选）
            System.out.println("Message not found");
        }
    }

}
