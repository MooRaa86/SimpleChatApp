package com.websocket.chat_app.controller;

import com.websocket.chat_app.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public MessageDto sendMessage(@Payload MessageDto message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chat")
    public MessageDto addUser(@Payload MessageDto message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

}
