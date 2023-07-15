package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.ISensorDataRepository;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.IllegalArgumentException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.web.dtos.responses.GetSensorDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static UserDetailsImpl getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

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

    @Override
    public BaseResponse get(String monitorId, String sensorName, String startTimestamp, String endTimestamp) {
        LocalDateTime now = LocalDateTime.now();

        if (startTimestamp.isEmpty() || endTimestamp.isEmpty()) {
            startTimestamp = now.minusDays(7).toString();
            endTimestamp = now.toString();
        }

        LocalDateTime startDateTime = LocalDateTime.parse(startTimestamp);
        LocalDateTime endDateTime = LocalDateTime.parse(endTimestamp);

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start timestamp must be before end timestamp");
        }

        if (!getUserAuthenticated().getMonitorIds().contains(monitorId)) {
            throw new ObjectNotFoundException("Monitor not found");
        }

        List<SensorData> sensorDataList = new ArrayList<>();

        if (sensorName.isEmpty()) {
            sensorDataList = repository.findAllByMonitorIdAndTimestampBetween(monitorId, startDateTime, endDateTime);
        } else {
            sensorDataList = repository.findAllByMonitorIdAndNameAndTimestampBetween(monitorId, sensorName,  startDateTime, endDateTime);
        }

        return BaseResponse.builder()
                .data(toGetSensorDataResponse(sensorDataList))
                .message("Sensor data found successfully!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }

    private GetSensorDataResponse toGetSensorDataResponse(List<SensorData> sensorDataList) {
        GetSensorDataResponse response = new GetSensorDataResponse();

        List<SensorData> temperatureDataList = new ArrayList<>();
        List<SensorData> movementDataList = new ArrayList<>();
        List<SensorData> soundDataList = new ArrayList<>();
        List<SensorData> humidityDataList = new ArrayList<>();

        sensorDataList.forEach(sensorData -> {
            switch (sensorData.getName()) {
                case "temperature":
                    temperatureDataList.add(sensorData);
                    break;
                case "movement":
                    movementDataList.add(sensorData);
                    break;
                case "sound":
                    soundDataList.add(sensorData);
                    break;
                case "humidity":
                    humidityDataList.add(sensorData);
                    break;
            }
        });

        response.setTemperature(temperatureDataList);
        response.setMovement(movementDataList);
        response.setSound(soundDataList);
        response.setHumidity(humidityDataList);

        return response;
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
