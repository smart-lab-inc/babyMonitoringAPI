package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;

import java.util.List;

public interface ISensorDataService {
    SensorData create(CreateSensorData createSensorData);

    BaseResponse createMany (List<CreateSensorData> createSensorData);
}
