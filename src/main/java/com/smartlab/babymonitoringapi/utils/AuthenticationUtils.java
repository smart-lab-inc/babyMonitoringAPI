package com.smartlab.babymonitoringapi.utils;

import com.smartlab.babymonitoringapi.services.impls.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static UserDetailsImpl getUserAuthenticatedFrom(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
