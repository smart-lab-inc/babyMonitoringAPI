package com.smartlab.babymonitoringapi.persistance.mongo.repositories;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISensorDataRepository extends MongoRepository<SensorData, String> {
}
