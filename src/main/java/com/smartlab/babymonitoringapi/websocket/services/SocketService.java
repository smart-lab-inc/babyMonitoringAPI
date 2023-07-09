package com.smartlab.babymonitoringapi.websocket.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.smartlab.babymonitoringapi.constants.MessageType;
import com.smartlab.babymonitoringapi.websocket.dtos.Message;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SocketService {
    public void sendMessage(String body, String room, String eventName, SocketIOClient senderClient) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(room).getClients();

        for (SocketIOClient client : clients) {
            Message message = from(body, room);

            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName, message);
            }
        }
    }

    private Message from(String body, String room) {
        return Message.builder()
                .body(body)
                .room(room)
                .messageType(MessageType.SERVER)
                .build();
    }
}
