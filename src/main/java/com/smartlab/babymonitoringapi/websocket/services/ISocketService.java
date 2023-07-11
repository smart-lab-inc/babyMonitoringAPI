package com.smartlab.babymonitoringapi.websocket.services;

import com.corundumstudio.socketio.SocketIOClient;

public interface ISocketService {
    void sendDataToSocket(String namespace, String roomId, String event, Object data, String message, Boolean success);

    void sendErrorMessageToSocket(SocketIOClient client, String message);
}
