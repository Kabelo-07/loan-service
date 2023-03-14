package com.loans.scheduler;

import com.loans.domain.*;
import com.loans.events.publisher.DomainEventPublisher;
import com.loans.repository.CustomerRepository;
import com.loans.repository.LoanAccountRepository;
import com.loans.repository.ProductRepository;
import com.loans.service.CustomerService;
import com.loans.service.LoanAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AutoPaymentSchedulerTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanAccountService loanAccountService;

    @MockBean
    private DomainEventPublisher publisher;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanAccountRepository loanAccountRepository;

    @Autowired
    private ProductRepository productRepository;

    private AutoPaymentScheduler paymentScheduler;

    @BeforeEach
    public void setUp() {
        paymentScheduler = new AutoPaymentScheduler(loanAccountService, customerService);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        loanAccountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void debitOutstandingAccounts() {
        //given a product exists
        Product product = productRepository.save(Product.newInstance("Product A1", 1000, 10, 15));

        //and a customer has been added
        Customer customer = customerRepository.save(Customer.newInstance("Test",
                "User", "email@one.com", "01222222", CommunicationMethod.EMAIL));
        //and has a virtual account with positive balance
        customer = customerService.creditVirtualAccount(customer, BigDecimal.valueOf(5000L));
        assertEquals(5000, customer.getVirtualAccount().getBalance().doubleValue());

        //and a loan account exists for a customer with auto_deduction payment method and past due date
        LoanAccount loanAccount = loanAccountRepository.save(LoanAccount.newInstance(
                product.getMaxAllowedLimit(),
                product.getMaxAllowedLimit(),
                product.getId(),
                Instant.now().minus(20, ChronoUnit.MINUTES),
                customer.getId(),
                PaymentMethod.AUTO_DEDUCTION
        ));

        assertTrue(loanAccount.isActive());

        //when the scheduled "job" runs
        paymentScheduler.debitOutstandingAccounts();

        //then the loan account is closed
        loanAccount = loanAccountRepository.findById(loanAccount.getId()).get();
        assertTrue(loanAccount.isClosed());

        //and the customer virtual account is debited
        customer = customerRepository.findById(customer.getId()).get();
        assertEquals(4000, customer.getVirtualAccount().getBalance().doubleValue());
    }
}