package com.smartlab.babymonitoringapi.rabbit;

import com.smartlab.babymonitoringapi.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    @Autowired
    private ISensorDataService sensorDataService;

    @RabbitListener(queues = {"${queue.on-new-sensor-data}"})
    public void onNewSensorData(List<CreateSensorData> createSensorData) {
        sensorDataService.createMany(createSensorData);
    }

}
