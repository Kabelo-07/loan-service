package com.loans.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testEqualsWhenCustomerObjects_haveDifferentIds() {
        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());

        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());

        assertNotEquals(customer1, customer2);
        assertNotEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void testEqualsWhenCustomersObjects_haveSameId() {
        UUID id = UUID.randomUUID();

        Customer customer1 = new Customer();
        customer1.setId(id);

        Customer customer2 = new Customer();
        customer2.setId(id);

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void test_canCreditVirtualAccount() {
        //given customer is created
        Customer customer = Customer.newInstance("Jon",
                "Snow", "js@js.com",
                "9129212121", CommunicationMethod.EMAIL);
        //and has virtual account with zero balance
        assertNotNull(customer.getVirtualAccount());
        assertEquals(0, customer.getVirtualAccount().getBalance().doubleValue());

        //when the virtual account is credited
        customer.creditVirtualAccount(BigDecimal.valueOf(3500));
        //then the balance increases
        assertEquals(3500, customer.getVirtualAccount().getBalance().doubleValue());
        //and when the virtual account is credited again
        customer.creditVirtualAccount(BigDecimal.valueOf(250));
        //then the balance increases
        assertEquals(3750, customer.getVirtualAccount().getBalance().doubleValue());
    }

    @Test
    void test_canDebitVirtualAccount() {
        //given customer is created
        Customer customer = Customer.newInstance("Jon",
                "Snow", "js@js.com",
                "9129212121", CommunicationMethod.EMAIL);
        //and has virtual account with zero balance
        assertNotNull(customer.getVirtualAccount());
        assertEquals(0, customer.getVirtualAccount().getBalance().doubleValue());

        //when the virtual account is credited
        customer.creditVirtualAccount(BigDecimal.valueOf(3500));
        //then the balance increases
        assertEquals(3500, customer.getVirtualAccount().getBalance().doubleValue());
        //and when the virtual account is debited
        customer.debitVirtualAccount(BigDecimal.valueOf(2900));
        //then the balance decreases
        assertEquals(600, customer.getVirtualAccount().getBalance().doubleValue());
    }
}