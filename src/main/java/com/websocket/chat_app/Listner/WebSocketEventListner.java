package com.websocket.chat_app.Listner;


import com.websocket.chat_app.dto.MessageDto;
import com.websocket.chat_app.dto.MsgType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListner {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");

        if(Objects.nonNull(username)) {
            log.info(username + " disconnected");

            messagingTemplate.convertAndSend("/topic/chat",
                    MessageDto.builder()
                            .msgType(MsgType.LEAVE)
                            .sender(username)
                            .build());
        }

    }
}
