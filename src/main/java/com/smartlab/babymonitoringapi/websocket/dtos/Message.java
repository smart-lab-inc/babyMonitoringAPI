package com.smartlab.babymonitoringapi.websocket.dtos;

import com.smartlab.babymonitoringapi.constants.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Message {

    private MessageType messageType;

    private String body;

    private String room;
}
