package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

import java.util.List;

public interface ISensorDataService {
    SensorData create(CreateSensorData createSensorData);

    BaseResponse createMany (List<CreateSensorData> createSensorData);
}
