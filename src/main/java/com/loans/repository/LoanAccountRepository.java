package com.loans.repository;

import com.loans.domain.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, UUID> {

    @Query("SELECT la FROM LoanAccount la WHERE la.accountStatus = 'ACTIVE' AND la.paymentMethod = 'AUTO_DEDUCTION' ")
    List<LoanAccount> findAllActiveAutoPaymentLoans();
}
