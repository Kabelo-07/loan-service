package com.loans.service.impl;

import com.loans.api.api.model.CommunicationMethodDTO;
import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Customer;
import com.loans.events.AccountCredited;
import com.loans.events.AccountDebited;
import com.loans.events.publisher.DomainEventPublisher;
import com.loans.repository.CustomerRepository;
import com.loans.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerRepository repository;

    @MockBean
    private DomainEventPublisher publisher;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(repository, publisher);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void test_createCustomer() {
        Customer customer = customerService.createCustomer(new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("0111231234")
                .emailAddress("user@email.com")
                .firstName("User")
                .lastName("Test"));
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Test
    void test_createCustomerWithSameEmail() {
        CustomerDTO customerDTO1 = new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("0111231234")
                .emailAddress("user@email.com")
                .firstName("User")
                .lastName("Test");

        CustomerDTO customerDTO2 = new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("0881231234")
                .emailAddress("user@email.com")
                .firstName("User")
                .lastName("Test");

        customerService.createCustomer(customerDTO1);

        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(customerDTO2));
    }

    @Test
    void test_createCustomerWithSamePhoneNumber() {
        CustomerDTO customerDTO1 = new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("0111231234")
                .emailAddress("user@email.com")
                .firstName("User")
                .lastName("Test");

        CustomerDTO customerDTO2 = new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("0111231234")
                .emailAddress("another@email.com")
                .firstName("User")
                .lastName("Test");

        customerService.createCustomer(customerDTO1);

        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(customerDTO2));
    }

    @Test
    void test_canCreditAndDebitVirtualAccount() {
        //given customer is created with virtual account
        Customer customer = customerService.createCustomer(new CustomerDTO()
                .communicationMethod(CommunicationMethodDTO.SMS)
                .phoneNumber("099121231312")
                .emailAddress("user@email.com")
                .firstName("User")
                .lastName("Test"));
        assertNotNull(customer);
        assertTrue(customer.hasVirtualAccount());
        assertEquals(0, customer.getVirtualAccount().getBalance().doubleValue());

        //when we credit customer account
        customer = customerService.creditVirtualAccount(customer, BigDecimal.valueOf(25000L));
        customer = customerService.creditVirtualAccount(customer, BigDecimal.valueOf(450L));
        //then account credited events are emitted
        verify(publisher, times(2)).publish(any(AccountCredited.class));

        //and debit customer account
        customer = customerService.debitVirtualAccount(customer.getId(), BigDecimal.valueOf(1500L));
        //and a single account debited event is emitted
        verify(publisher, times(1)).publish(any(AccountDebited.class));

        //and the balance should be correct
        assertEquals(23950, customer.getVirtualAccount().getBalance().doubleValue());
    }

}