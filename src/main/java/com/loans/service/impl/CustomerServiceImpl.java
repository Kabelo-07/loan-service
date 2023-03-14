package com.loans.service.impl;

import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Customer;
import com.loans.domain.VirtualAccount;
import com.loans.events.AccountCredited;
import com.loans.events.AccountDebited;
import com.loans.events.publisher.DomainEventPublisher;
import com.loans.exceptions.NotFoundException;
import com.loans.mapper.CustomerMapper;
import com.loans.repository.CustomerRepository;
import com.loans.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final DomainEventPublisher publisher;

    public CustomerServiceImpl(CustomerRepository repository,
                               DomainEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public Customer createCustomer(CustomerDTO dto) {
        Customer customer = CustomerMapper.INSTANCE.toEntity(dto);
        customer.setVirtualAccount(new VirtualAccount());

        return repository.save(customer);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<Customer> findById(UUID customerId) {
        return repository.findById(customerId);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer creditVirtualAccount(Customer customer, BigDecimal amount) {
        customer.creditVirtualAccount(amount);
        customer = repository.save(customer);

        publisher.publish(new AccountCredited(customer, amount));
        return customer;
    }

    @Override
    public Customer debitVirtualAccount(UUID customerId, BigDecimal amount) {
        Customer customer = findById(customerId)
                .orElseThrow(() -> new NotFoundException(String.format("Customer %s not found", customerId)));

        customer.debitVirtualAccount(amount);
        customer = repository.save(customer);

        publisher.publish(new AccountDebited(customer, amount));
        return customer;
    }
}
