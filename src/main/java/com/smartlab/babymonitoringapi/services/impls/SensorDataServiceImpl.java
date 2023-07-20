package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.ISensorDataRepository;
import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.utils.StatisticsUtils;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.IllegalArgumentException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.web.dtos.responses.GetSensorDataResponse;
import com.smartlab.babymonitoringapi.web.dtos.responses.GetStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.smartlab.babymonitoringapi.utils.AuthenticationUtils.getUserAuthenticated;

@Service
public class SensorDataServiceImpl implements ISensorDataService {

    @Autowired
    private ISensorDataRepository repository;

    @Autowired
    private IMonitorService monitorService;

    private List<String> monitorIds = getUserAuthenticated().getMonitorIds();

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
        List<LocalDateTime> dateTimes = validateDateTimes(startTimestamp, endTimestamp);

        LocalDateTime startDateTime = dateTimes.get(0);
        LocalDateTime endDateTime = dateTimes.get(1);

        if (!monitorIds.contains(monitorId)) {
            throw new ObjectNotFoundException("Monitor not found");
        }

        List<SensorData> sensorDataList = getSensorDataList(monitorId, sensorName, startDateTime, endDateTime);

        return BaseResponse.builder()
                .data(toGetSensorDataResponse(sensorDataList))
                .message("Sensor data found successfully!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }

    @Override
    public BaseResponse getStatistics(String monitorId, String sensorName, String startTimestamp, String endTimestamp) {
        List<LocalDateTime> dateTimes = validateDateTimes(startTimestamp, endTimestamp);

        LocalDateTime startDateTime = dateTimes.get(0);
        LocalDateTime endDateTime = dateTimes.get(1);

        if (!monitorIds.contains(monitorId)) {
            throw new ObjectNotFoundException("Monitor not found");
        }

        List<SensorData> sensorDataList = getSensorDataList(monitorId, sensorName, startDateTime, endDateTime);

        GetSensorDataResponse dataList = toGetSensorDataResponse(sensorDataList);
        Map<String, Float> temperatureStatistics = new HashMap<>();
        Map<String, Float> movementStatistics = new HashMap<>();
        Map<String, Float> soundStatistics = new HashMap<>();
        Map<String, Float> humidityStatistics = new HashMap<>();

        if (dataList.getTemperature().size() != 0) {
            temperatureStatistics = StatisticsUtils.calculateStatistics(dataList.getTemperature());
        }
        if (dataList.getMovement().size() != 0) {
            movementStatistics = StatisticsUtils.calculateStatistics(dataList.getMovement());
        }
        if (dataList.getSound().size() != 0) {
            soundStatistics = StatisticsUtils.calculateStatistics(dataList.getSound());
        }
        if (dataList.getHumidity().size() != 0) {
            humidityStatistics = StatisticsUtils.calculateStatistics(dataList.getHumidity());
        }

        return BaseResponse.builder()
                .data(toGetStatisticsResponse(temperatureStatistics,
                        movementStatistics,
                        soundStatistics,
                        humidityStatistics))
                .message("Sensor data statistics found successfully!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }

    private List<SensorData> getSensorDataList(String monitorId, String sensorName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<SensorData> sensorDataList = new ArrayList<>();

        if (sensorName.isEmpty()) {
            sensorDataList = repository.findAllByMonitorIdAndTimestampBetween(monitorId, startDateTime, endDateTime);
        } else {
            sensorDataList = repository.findAllByMonitorIdAndNameAndTimestampBetween(monitorId, sensorName,  startDateTime, endDateTime);
        }

        return sensorDataList;
    }

    private List<LocalDateTime> validateDateTimes(String startTimestamp, String endTimestamp) {
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

        return List.of(startDateTime, endDateTime);
    }

    private GetStatisticsResponse toGetStatisticsResponse(Map<String, Float> temperatureStatistics,
                                                          Map<String, Float> movementStatistics,
                                                          Map<String, Float> soundStatistics,
                                                          Map<String, Float> humidityStatistics) {
        GetStatisticsResponse response = new GetStatisticsResponse();

        response.setTemperature(temperatureStatistics);
        response.setMovement(movementStatistics);
        response.setSound(soundStatistics);
        response.setHumidity(humidityStatistics);

        return response;
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
