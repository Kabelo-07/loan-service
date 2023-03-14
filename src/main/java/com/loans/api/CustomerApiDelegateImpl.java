package com.loans.api;

import com.loans.api.api.CustomersApiDelegate;
import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Customer;
import com.loans.mapper.CustomerMapper;
import com.loans.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class CustomerApiDelegateImpl implements CustomersApiDelegate {

    private final CustomerService customerService;

    public CustomerApiDelegateImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<List<CustomerDTO>> listCustomers() {
        List<Customer> customers = customerService.findAll();
        if (CollectionUtils.isEmpty(customers)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CustomerMapper.INSTANCE.toDto(customers));
    }
}
