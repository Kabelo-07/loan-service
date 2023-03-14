package com.loans.service;

import com.loans.api.api.model.PaymentMethodDTO;
import com.loans.domain.LoanAccount;
import com.loans.domain.Product;

import java.util.List;
import java.util.UUID;

public interface LoanAccountService {

    LoanAccount createLoanAccount(Product product, UUID customerId, PaymentMethodDTO paymentMethod);

    List<LoanAccount> findAllActiveAutoPaymentLoans();

    LoanAccount closeAccount(UUID accountId);
}
