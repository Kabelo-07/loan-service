package com.loans.events;

import com.loans.domain.Customer;

import java.math.BigDecimal;

public class AccountCredited extends AccountEvent {

    public AccountCredited(Customer customer, BigDecimal amount) {
        super(customer, amount);
    }
}
