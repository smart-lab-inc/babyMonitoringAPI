package com.smartlab.babymonitoringapi.rabbit;

import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataRequest;
import com.smartlab.babymonitoringapi.services.ICryDetectionService;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.websocket.services.ISocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Consumer {

    @Autowired
    private ISensorDataService sensorDataService;

    @Autowired
    private ISocketService socketService;

    @Autowired
    private ICryDetectionService cryDetectionService;

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

        processCryDetection(request);

        if (request.getIsReadyToStore().equals("true")) {
            saveSensorData(request.getBody(), request.getMonitorId());
        }
    }

    private void saveSensorData(List<NewSensorDataBodyRequest> body, String monitorId) {
        sensorDataService.createManyWithSameMonitorId(body, monitorId);
    }

    private void processCryDetection(NewSensorDataRequest newSensorDataRequest) {
        Map<String, Boolean> data = new HashMap<>();

        newSensorDataRequest.getBody().forEach(sensorData -> {
            if (sensorData.getName().equals("moved")) {
                data.put("moved", sensorData.getValue() > 0);
            } else if (sensorData.getName().equals("sound")) {
                data.put("sound", sensorData.getValue() > 0);
            }
        });

        cryDetectionService.processCryDetection(
                newSensorDataRequest.getMonitorId(),
                data.get("moved"),
                data.get("sound")
        );
    }

}
