package com.enclave.FaceRecognition.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

public class PublicEndpointRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.getMethodAnnotation(PublicEndpoint.class) != null) {
                return true;
            }
            if (handlerMethod.getBeanType().isAnnotationPresent(PublicEndpoint.class)) {
                return true;
            }
        }
        return false;
    }
}
