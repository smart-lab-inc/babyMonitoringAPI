package com.smartlab.babymonitoringapi.rabbit;

import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataRequest;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.websocket.services.ISocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    @Autowired
    private ISensorDataService sensorDataService;

    @Autowired
    private ISocketService socketService;

    @Value("${websocket.namespace.monitoring}")
    private String monitoringNamespace;

    @Value("${websocket.namespace.monitoring.events.onNewSensorData}")
    private String onNewSensorDataEventMonitoringNamespace;

    @RabbitListener(queues = {"${exchange.queue.onNewSensorData.name}"})
    public void onNewSensorData(NewSensorDataRequest request) {
        socketService.sendDataToSocket(
                monitoringNamespace,
                request.getMonitorId(),
                onNewSensorDataEventMonitoringNamespace,
                request.getBody(),
                "New sensor data received",
                true
        );
        saveSensorData(request.getBody(), request.getMonitorId());
    }

    private void saveSensorData(List<NewSensorDataBodyRequest> body, String monitorId) {
        sensorDataService.createManyWithSameMonitorId(body, monitorId);
    }

}
