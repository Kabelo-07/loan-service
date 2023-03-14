package com.loans.service.impl;

import com.loans.model.NotificationDTO;
import com.loans.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("EMAIL")
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(NotificationDTO dto) {
      log.info("Sending a Email notification {}", dto);
    }
}
