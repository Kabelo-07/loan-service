package com.loans.events;

import com.loans.domain.Customer;

import java.math.BigDecimal;

public class AccountDebited extends AccountEvent {

    public AccountDebited(Customer customer, BigDecimal amount) {
        super(customer, amount);
    }
}
