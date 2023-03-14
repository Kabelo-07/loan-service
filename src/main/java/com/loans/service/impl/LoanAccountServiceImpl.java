package com.loans.service.impl;

import com.loans.api.api.model.PaymentMethodDTO;
import com.loans.domain.LoanAccount;
import com.loans.domain.PaymentMethod;
import com.loans.domain.Product;
import com.loans.exceptions.NotFoundException;
import com.loans.repository.LoanAccountRepository;
import com.loans.service.LoanAccountService;
import com.loans.util.Calculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LoanAccountServiceImpl implements LoanAccountService {

    private final LoanAccountRepository repository;

    public LoanAccountServiceImpl(LoanAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanAccount createLoanAccount(Product product, UUID customerId, PaymentMethodDTO paymentMethod) {
        LoanAccount loan = new LoanAccount();
        loan.setRepaymentAmount(Calculator.calculateAmount(product.getMaxAllowedLimit(), product.getInterestPercentage()));
        loan.setPrincipalAmount(product.getMaxAllowedLimit());
        loan.setProductId(product.getId());
        loan.setCustomerId(customerId);
        loan.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.name()));
        loan.setDueDate(Instant.now().plus(product.getTenure(), ChronoUnit.DAYS));

        return repository.save(loan);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<LoanAccount> findAllActiveAutoPaymentLoans() {
        return repository.findAllActiveAutoPaymentLoans();
    }

    @Override
    public LoanAccount closeAccount(UUID accountId) {
        LoanAccount account = repository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Loan account"));

        account.markAsClosed();
        return repository.save(account);
    }
}
