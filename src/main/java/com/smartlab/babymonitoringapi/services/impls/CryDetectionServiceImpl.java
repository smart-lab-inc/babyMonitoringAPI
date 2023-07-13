package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.constants.SMSType;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.services.ICryDetectionService;
import com.smartlab.babymonitoringapi.services.ISNSService;
import com.smartlab.babymonitoringapi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CryDetectionServiceImpl implements ICryDetectionService {

    @Autowired
    private ISNSService snsService;

    @Autowired
    private IUserService userService;

    private final Map<String, Boolean> cryDetectionMap = new HashMap<>();

    private int cryDetectionDuration = 6;

    @Override
    public void processCryDetection(String monitorId, Boolean isMovementDetected, Boolean isSoundDetected) {
        String message = "Your baby probably is crying, check the camera.";

        if (isMovementDetected && isSoundDetected) {
            if (cryDetectionMap.containsKey(monitorId)) {
                cryDetectionMap.put(monitorId, true);
            } else {
                cryDetectionMap.put(monitorId, false);
            }
        } else {
            cryDetectionMap.remove(monitorId);
        }

        if (cryDetectionMap.getOrDefault(monitorId, false)) {
            cryDetectionDuration--;
        } else {
            cryDetectionDuration = 6;
        }

        User user = userService.findOneAndEnsureExistByMonitorId(monitorId);

        if (cryDetectionDuration == 0) {
            snsService.sendSMS(message, user.getPhoneNumber(), SMSType.TRANSACTIONAL);
            cryDetectionDuration = 6;
        }

    }
}
