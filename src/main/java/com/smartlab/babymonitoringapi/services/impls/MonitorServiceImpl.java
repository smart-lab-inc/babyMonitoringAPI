package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.IMonitorRepository;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.utils.AuthenticationUtils;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.AccessDeniedException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateMonitorRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.web.dtos.responses.GetMonitorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    private IMonitorRepository repository;

    @Autowired
    private IUserService userService;

    @Override
    public BaseResponse create(CreateMonitorRequest request) {
        String serialNumber = UUID.randomUUID().toString();

        Monitor monitor = Monitor.builder().serialNumber(serialNumber).userId(request.getUserId()).isActivated(Boolean.FALSE).build();
        Monitor savedMonitor = repository.save(monitor);

        User user = userService.findOneAndEnsureExistById(request.getUserId());

        if (user.getMonitorIds() == null) {
            user.setMonitorIds(new ArrayList<>());
        }

        user.getMonitorIds().add(savedMonitor.getId());
        userService.update(user);

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

    @Override
    public Monitor update(Monitor monitor) {
        return repository.save(monitor);
    }

    @Override
    public Monitor findOneAndEnsureExistBySerialNumber(String serialNumber) {
        return repository.findOneBySerialNumber(serialNumber).orElseThrow(() -> new ObjectNotFoundException("Monitor not found"));
    }

    @Override
    public BaseResponse listByUserId(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userAuthenticatedId = AuthenticationUtils.getUserAuthenticatedFrom(authentication).getUser().getId();

        if (!userAuthenticatedId.equals(userId)) {
            throw new AccessDeniedException();
        }

        List<Monitor> monitorList = repository.findAllByUserId(userId);

        List<GetMonitorResponse> monitorResponseList = monitorList.stream().map(this::toGetMonitorResponse).toList();


        return BaseResponse.builder()
                .data(monitorResponseList)
                .message("Monitors found")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(Boolean.TRUE)
                .build();
    }

    GetMonitorResponse toGetMonitorResponse(Monitor monitor) {
        GetMonitorResponse response = new GetMonitorResponse();

        response.setId(monitor.getId());
        response.setSerialNumber(monitor.getSerialNumber());
        response.setUserId(monitor.getUserId());

        return response;
    }

}
