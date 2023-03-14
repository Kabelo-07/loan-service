package com.loans.service;

import com.loans.domain.CommunicationMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationContext {

    private final Map<String, NotificationService> serviceMap;

    public NotificationContext(Map<String, NotificationService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public NotificationService resolve(CommunicationMethod method) {
        NotificationService notificationService = serviceMap.get(method.name());

        if (null == notificationService) {
            throw new IllegalArgumentException(
                    String.format("Unable to resolve notification service for comms method %s", method.name())
            );
        }

        return notificationService;
    }
}
