package com.smartlab.babymonitoringapi.persistance.mongo.repositories;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMonitorRepository extends MongoRepository<Monitor, String> {

    Optional<Monitor> findOneById(String id);

    Optional<Monitor> findOneBySerialNumber(String serialNumber);

    @Query(value = "{ 'userId' : ?0 }")
    List<Monitor> findAllByUserId(String userId);
}
