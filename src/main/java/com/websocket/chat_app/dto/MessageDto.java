package com.websocket.chat_app.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MessageDto {

    private MsgType msgType;
    private String content;
    private String sender;
}
