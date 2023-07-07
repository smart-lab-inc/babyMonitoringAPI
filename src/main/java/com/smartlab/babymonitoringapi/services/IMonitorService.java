package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

public interface IMonitorService {
    Monitor findOneAndEnsureExistById(String id);

    BaseResponse create();
}
