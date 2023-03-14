package com.loans.service;

import com.loans.model.NotificationDTO;

public interface NotificationService {

    void send(NotificationDTO dto);
}
