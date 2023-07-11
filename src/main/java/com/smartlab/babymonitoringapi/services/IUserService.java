package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

public interface IUserService {

    BaseResponse create(CreateUserRequest request);

    BaseResponse get(String id);

    BaseResponse update(UpdateUserRequest request, String id);

    BaseResponse delete(String id);

    User findOneAndEnsureExistById(String id);

    BaseResponse getByMonitorId(String id);
}
