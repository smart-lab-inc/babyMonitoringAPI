package com.smartlab.babymonitoringapi.websocket.services.impls;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.smartlab.babymonitoringapi.constants.MessageType;
import com.smartlab.babymonitoringapi.websocket.dtos.Message;
import com.smartlab.babymonitoringapi.websocket.services.ISocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketServiceImpl implements ISocketService {

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void sendDataToSocket(String namespace, String roomId, String event, Object data, String message, Boolean success) {
        Message messageObject = from(data, message, success);
        socketIOServer.getNamespace(namespace).getRoomOperations(roomId).sendEvent(event, messageObject);
    }

    @Override
    public void sendErrorMessageToSocket(SocketIOClient client, String message) {
        Message messageObject = from(null, message, false);
        client.sendEvent("error", messageObject);
    }

    private Message from(Object data, String message, Boolean success) {
        return Message.builder()
                .data(data)
                .message(message)
                .success(success)
                .type(MessageType.SERVER)
                .build();
    }
}
