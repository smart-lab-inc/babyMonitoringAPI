package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.ISensorDataRepository;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataServiceImpl implements ISensorDataService {

    @Autowired
    private ISensorDataRepository repository;

    @Override
    public SensorData create(CreateSensorData createSensorData) {
        return null;
    }

    @Override
    public BaseResponse createMany(List<CreateSensorData> createSensorData) {
        List<SensorData> dataList = createSensorData.stream().map(this::toSensorData).toList();

        List<SensorData> savedDataList = repository.saveAll(dataList);

        return BaseResponse.builder()
                .data(savedDataList)
                .message("Sensor data saved successfully!")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    private SensorData toSensorData(CreateSensorData createSensorData) {
        return SensorData.builder()
                .name(createSensorData.getName())
                .value(createSensorData.getValue())
                .measurement(createSensorData.getMeasurement())
                .build();
    }
}
