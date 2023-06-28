package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.dtos.requests.CreateSensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.ISensorDataRepository;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataServiceImpl implements ISensorDataService {

    @Autowired
    private ISensorDataRepository repository;

    @Override
    public com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData create(CreateSensorData createSensorData) {
        return null;
    }

    @Override
    public List<SensorData> createMany(List<CreateSensorData> createSensorData) {
        List<com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData> dataList = createSensorData.stream().map(this::toSensorData).toList();

        List<com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData> savedDataList = repository.saveAll(dataList);

        return savedDataList;
    }

    private com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData toSensorData(CreateSensorData createSensorData) {
        return com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData.builder()
                .name(createSensorData.getName())
                .value(createSensorData.getValue())
                .measurement(createSensorData.getMeasurement())
                .build();
    }
}
