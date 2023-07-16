package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserMonitorRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

import java.util.Optional;

public interface IUserService {

    BaseResponse create(CreateUserRequest request);

    BaseResponse get(String id);

    BaseResponse update(UpdateUserRequest request, String id);

    User update(User user);

    BaseResponse delete(String id);

    User findOneAndEnsureExistById(String id);

    Optional<User> findOneByMonitorId(String id);

    User findOneAndEnsureExistByMonitorId(String id);
    
    BaseResponse update(UpdateUserMonitorRequest request);
}
