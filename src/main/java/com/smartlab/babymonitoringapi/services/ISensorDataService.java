package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;

import java.util.List;

public interface ISensorDataService {
    com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData create(CreateSensorData createSensorData);

    List<SensorData> createMany (List<CreateSensorData> createSensorData);
}
