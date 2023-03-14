package com.loans.service;

import com.loans.domain.CommunicationMethod;
import com.loans.service.impl.EmailNotificationService;
import com.loans.service.impl.SmsNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationContextTest {

    private NotificationContext context;

    @BeforeEach
    public void setUp() {
        Map<String, NotificationService> map = new HashMap<>();
        map.put(CommunicationMethod.EMAIL.name(), new EmailNotificationService());
        map.put(CommunicationMethod.SMS.name(), new SmsNotificationService());

        context = new NotificationContext(map);
    }

    @Test
    void test_canResolveService_withCommunicationMethod() {
        NotificationService service = context.resolve(CommunicationMethod.EMAIL);
        assertTrue(service instanceof EmailNotificationService);

        service = context.resolve(CommunicationMethod.SMS);
        assertTrue(service instanceof SmsNotificationService);
    }

    @Test
    void test_cannotResolveService_withNullCommunicationMethod() {
        assertThrows(NullPointerException.class, () -> context.resolve(null));
    }
}