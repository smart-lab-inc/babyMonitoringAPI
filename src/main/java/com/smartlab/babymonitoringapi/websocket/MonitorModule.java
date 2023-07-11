package com.smartlab.babymonitoringapi.websocket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.utils.JWTUtils;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.websocket.dtos.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class MonitorModule {

    @Value("${smartlab.app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private IUserService userService;

    public MonitorModule(SocketIOServer server) {
        SocketIONamespace monitorNamespace = server.addNamespace("/monitor");
        monitorNamespace.addConnectListener(onConnected());
        monitorNamespace.addDisconnectListener(onDisconnected());
        monitorNamespace.addEventListener("send_message", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
//            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data.getBody());

        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            String monitorId = client.getHandshakeData().getSingleUrlParam("monitorId");

            if (!StringUtils.hasText(token) || !JWTUtils.isValidateToken(token, jwtSecret)) {
                client.disconnect();
            }

            String emailFromToken = JWTUtils.getEmailFromJWT(token, jwtSecret);
            BaseResponse response = userService.getByMonitorId(monitorId);
            User user = (User) response.getData();

            if (!StringUtils.hasText(monitorId) || !emailFromToken.equals(user.getEmail())) {
                client.disconnect();
            }

            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
    }

}
