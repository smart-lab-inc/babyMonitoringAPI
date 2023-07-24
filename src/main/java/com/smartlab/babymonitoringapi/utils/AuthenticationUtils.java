package com.smartlab.babymonitoringapi.utils;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.services.impls.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static User getUserAuthenticatedFrom(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
