package com.smartlab.babymonitoringapi.websocket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.utils.JWTUtils;
import com.smartlab.babymonitoringapi.websocket.services.ISocketService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MonitorModule {
    @Autowired
    private SocketIOServer server;

    @Autowired
    private ISocketService socketService;

    @Autowired
    private IUserService userService;

    @Value("${smartlab.app.jwtSecret}")
    private String jwtSecret;

    @Value("${websocket.namespace.monitoring}")
    private String monitoringNamespace;

    @PostConstruct
    private void init() {
        SocketIONamespace monitorNamespace = server.addNamespace(monitoringNamespace);
        monitorNamespace.addConnectListener(onConnected());
        monitorNamespace.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            String monitorId = client.getHandshakeData().getSingleUrlParam("monitorId");

            if (token == null || monitorId == null || token.isEmpty() || monitorId.isEmpty()) {
                socketService.sendErrorMessageToSocket(client, "Token or monitorId is needed");
                client.disconnect();
                return;
            }

            Optional<User> optionalUser = userService.findOneByMonitorId(monitorId);

            if (!JWTUtils.isValidateToken(token, jwtSecret) || optionalUser.isEmpty()) {
                socketService.sendErrorMessageToSocket(client, "Invalid token or monitorId");
                client.disconnect();
                return;
            }

            String emailFromJWT = JWTUtils.getEmailFromJWT(token, jwtSecret);

            if (!isEmailMatch(optionalUser.get().getEmail(), emailFromJWT)) {
                socketService.sendErrorMessageToSocket(client, "You are not the owner of this monitor");
                client.disconnect();
                return;
            }

            client.joinRoom(monitorId);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
    }

    private boolean isEmailMatch(String email1, String email2) {
        return email1.equals(email2);
    }

}
