package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.IMonitorRepository;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private IMonitorRepository repository;

    @Override
    public BaseResponse create() {
        String serialNumber = UUID.randomUUID().toString();

        Monitor monitor = Monitor.builder().serialNumber(serialNumber).build();

        Monitor savedMonitor = repository.save(monitor);

        return BaseResponse.builder()
                .data(savedMonitor)
                .message("Monitor created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(Boolean.TRUE)
                .build();
    }

    @Override
    public Monitor findOneAndEnsureExistById(String id) {
        return repository.findOneById(id).orElseThrow(() -> new RuntimeException("Monitor not found"));
    }
}
