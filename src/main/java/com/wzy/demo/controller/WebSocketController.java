package com.wzy.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzy.demo.common.Constast;
import com.wzy.demo.common.RedisService;
import com.wzy.demo.service.MessageService;
import com.wzy.demo.vo.MessageVo;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisService redisService;


    @MessageMapping("/send/{uuid}")
    public void handleMessage(@DestinationVariable String uuid, @Payload String message)
            throws JsonMappingException, JsonProcessingException {
        // 鉴权
        try {
            MessageVo messageVo = objectMapper.readValue(message, MessageVo.class);
            //判断是否是本人发起的消息
            if (redisService.getValue(uuid) == null
                    || Integer.parseInt((String)redisService.getValue(uuid) ) != messageVo.getSenderId()) {
                return;
            }
            messageVo.setSendTime(LocalDateTime.now());
            if (messageVo.getSenderId()
                    .equals(redisService.getValue(Constast.MESSAGE_FLAG + messageVo.getReceiverId()))) {
                messageVo.setHaveRead(true);
            }
            messageService.save(messageVo);
            String messageStr = objectMapper.writeValueAsString(messageVo);
            messagingTemplate.convertAndSend(
                    "/topic/messages/" + redisService.getValue("uuid" + messageVo.getReceiverId()), messageStr);
            messagingTemplate.convertAndSend("/topic/messages/" + redisService.getValue("uuid" + messageVo.getSenderId()), messageStr);
        } catch (Exception e) {
            log.error("send error", e);
        }

    }

}
