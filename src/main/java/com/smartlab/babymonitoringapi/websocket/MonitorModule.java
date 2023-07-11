package com.smartlab.babymonitoringapi.websocket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.utils.JWTUtils;
import com.smartlab.babymonitoringapi.websocket.dtos.Message;
import com.smartlab.babymonitoringapi.websocket.services.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MonitorModule {

    @Value("${smartlab.app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private IUserService userService;

    @Autowired
    private SocketService socketService;

    public MonitorModule(SocketIOServer server) {
        SocketIONamespace monitorNamespace = server.addNamespace("/monitor");
        monitorNamespace.addConnectListener(onConnected());
        monitorNamespace.addDisconnectListener(onDisconnected());
        monitorNamespace.addEventListener("send_sensor_data", Message.class, onSensorDataReceived());
    }

    private DataListener<Message> onSensorDataReceived() {
        return (senderClient, data, ackSender) -> socketService.sendMessage(data.getBody(), data.getRoom(), "get_sensor_data", senderClient);
    }

    private ConnectListener onConnected() {
        return (client) -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            String monitorId = client.getHandshakeData().getSingleUrlParam("monitorId");

            if (!StringUtils.hasText(token) || !JWTUtils.isValidateToken(token, jwtSecret)) {
                client.disconnect();
            }

            Optional<User> optionalUser = userService.getByMonitorId(monitorId);
            log.info("es vacio: " + optionalUser.isEmpty());
//
//            if (!StringUtils.hasText(monitorId) || optionalUser.isEmpty()) {
//                client.disconnect();
//            }

            client.joinRoom(monitorId);

            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
    }

}
