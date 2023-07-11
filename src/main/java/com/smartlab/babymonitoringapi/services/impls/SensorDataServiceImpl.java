package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.ISensorDataRepository;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataServiceImpl implements ISensorDataService {

    @Autowired
    private ISensorDataRepository repository;

    @Autowired
    private IMonitorService monitorService;

    @Override
    public BaseResponse createManyWithSameMonitorId(List<NewSensorDataBodyRequest> createSensorDatumRequests, String monitorId) {
        List<String> sensorDataIds = new ArrayList<>();

        List<SensorData> dataList = createSensorDatumRequests.stream().map(request -> {
            SensorData sensorData = toSensorData(request, monitorId);
            sensorDataIds.add(sensorData.getId());

            return sensorData;
        }).toList();

        List<SensorData> savedDataList = repository.saveAll(dataList);

        Monitor monitor = monitorService.findOneAndEnsureExistById(monitorId);

        if (monitor.getSensorDataIds() == null) {
            monitor.setSensorDataIds(new ArrayList<>());
        }

        monitor.getSensorDataIds().addAll(sensorDataIds);
        monitorService.update(monitor);

        return BaseResponse.builder()
                .data(savedDataList)
                .message("Sensor data saved successfully!")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    private SensorData toSensorData(NewSensorDataBodyRequest newSensorDataBodyRequest, String monitorId) {
        LocalDateTime timestamp = LocalDateTime.parse(newSensorDataBodyRequest.getTimestamp());

        return SensorData.builder()
                .name(newSensorDataBodyRequest.getName())
                .value(newSensorDataBodyRequest.getValue())
                .measurement(newSensorDataBodyRequest.getMeasurement())
                .timestamp(timestamp)
                .monitorId(monitorId)
                .build();
    }
}
