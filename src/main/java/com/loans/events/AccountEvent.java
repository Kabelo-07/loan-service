package com.loans.events;

import com.loans.domain.Customer;

import java.math.BigDecimal;
import java.util.StringJoiner;

public abstract class AccountEvent extends DomainEvent {

    private final Customer customer;
    private final BigDecimal amount;

    protected AccountEvent(Customer customer, BigDecimal amount) {
        this.customer = customer;
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountEvent.class.getSimpleName() + "[", "]")
                .add("customer=" + customer)
                .add("amount=" + amount)
                .toString();
    }
}
