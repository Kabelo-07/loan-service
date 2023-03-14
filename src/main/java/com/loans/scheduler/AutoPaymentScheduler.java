package com.loans.scheduler;

import com.loans.domain.LoanAccount;
import com.loans.service.CustomerService;
import com.loans.service.LoanAccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoPaymentScheduler {

    private final LoanAccountService loanService;
    private final CustomerService customerService;

    public AutoPaymentScheduler(LoanAccountService loanService,
                                CustomerService customerService) {
        this.loanService = loanService;
        this.customerService = customerService;
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void debitOutstandingAccounts() {
        loanService.findAllActiveAutoPaymentLoans()
                .stream()
                .filter(LoanAccount::hasDueDatePassed)
                .forEach(account -> {
                    customerService.debitVirtualAccount(account.getCustomerId(), account.getRepaymentAmount());
                    loanService.closeAccount(account.getId());
                });
    }
}
