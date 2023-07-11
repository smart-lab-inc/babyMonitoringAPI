package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.IMonitorRepository;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private IMonitorRepository repository;

    @Override
    public Monitor findOneAndEnsureExistById(String id) {
        return repository.findOneById(id).orElseThrow(() -> new RuntimeException("Monitor not found"));
    }
}
