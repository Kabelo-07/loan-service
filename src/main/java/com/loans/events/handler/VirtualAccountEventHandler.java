package com.loans.events.handler;

import com.loans.domain.Customer;
import com.loans.events.AccountCredited;
import com.loans.events.AccountDebited;
import com.loans.model.NotificationDTO;
import com.loans.service.CustomerService;
import com.loans.service.NotificationContext;
import com.loans.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VirtualAccountEventHandler {

    private final CustomerService service;
    private final NotificationContext context;

    public VirtualAccountEventHandler(CustomerService service, NotificationContext context) {
        this.service = service;
        this.context = context;
    }

    @EventListener
    @Async
    public void handleAccountCredited(AccountCredited accountCredited) {
        log.info("Handling event {}", accountCredited);

        Customer customer = accountCredited.getCustomer();

        NotificationService notificationService = context.resolve(customer.getCommunicationMethod());

        NotificationDTO dto = new NotificationDTO(String.format("Your account has been credited with %s", accountCredited.getAmount()));

        notificationService.send(dto);
    }

    @EventListener
    @Async
    public void handleAccountDebited(AccountDebited accountDebited) {
        Customer customer = accountDebited.getCustomer();

        NotificationService notificationService = context.resolve(customer.getCommunicationMethod());

        NotificationDTO dto = new NotificationDTO(String.format("Your account has been debited with %s", accountDebited.getAmount()));

        notificationService.send(dto);
    }
}
