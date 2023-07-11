package com.smartlab.babymonitoringapi.websocket.dtos;

import com.smartlab.babymonitoringapi.constants.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Object data;

    private String message;

    private MessageType type;

    private Boolean success;
}
