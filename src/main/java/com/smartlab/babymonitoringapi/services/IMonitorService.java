package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;

public interface IMonitorService {
    Monitor findOneAndEnsureExistById(String id);
}
