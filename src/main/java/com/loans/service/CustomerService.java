package com.loans.service;

import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(CustomerDTO dto);

    Optional<Customer> findById(UUID customerId);

    List<Customer> findAll();

    Customer creditVirtualAccount(Customer customer, BigDecimal amount);

    Customer debitVirtualAccount(UUID customerId, BigDecimal amount);
}
