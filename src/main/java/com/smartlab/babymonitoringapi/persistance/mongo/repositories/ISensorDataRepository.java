package com.smartlab.babymonitoringapi.persistance.mongo.repositories;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ISensorDataRepository extends MongoRepository<SensorData, String> {

    @Query(value = "{'monitorId': ?0, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<SensorData> findAllByMonitorIdAndTimestampBetween(String monitorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "{'monitorId': ?0, 'name': ?1, 'timestamp': {$gte: ?2, $lte: ?3}}")
    List<SensorData> findAllByMonitorIdAndNameAndTimestampBetween(String monitorId, String sensorName, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
