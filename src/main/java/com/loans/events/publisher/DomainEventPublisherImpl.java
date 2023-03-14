package com.loans.events.publisher;

import com.loans.events.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public DomainEventPublisherImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(@NotNull @Valid DomainEvent o) {
        publisher.publishEvent(o);
    }
}
