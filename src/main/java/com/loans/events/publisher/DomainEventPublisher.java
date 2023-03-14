package com.loans.events.publisher;

import com.loans.events.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent o);
}
