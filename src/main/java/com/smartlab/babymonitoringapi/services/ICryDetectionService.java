package com.smartlab.babymonitoringapi.services;

public interface ICryDetectionService {
    void processCryDetection(String monitorId, Boolean isMovementDetected, Boolean isSoundDetected);
}
