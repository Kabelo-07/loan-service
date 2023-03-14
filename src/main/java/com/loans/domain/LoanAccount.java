package com.loans.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Table(name = "loan_account")
public class LoanAccount extends AbstractEntity implements Serializable {

    /**
     * The total loan amount (with interest) borrowed by the customer
     */
    @NotNull
    @Column(name = "repayment_amount", nullable = false, scale = 2)
    @Min(1)
    private BigDecimal repaymentAmount;

    /**
     * The actual amount borrowed by the customer
     */
    @NotNull
    @Column(name = "principal_amount", nullable = false, scale = 2)
    @Min(1)
    private BigDecimal principalAmount;

    @NotNull
    @Column(nullable = false, name = "product_id")
    private UUID productId;

    @NotNull
    @Column(nullable = false, name = "due_date")
    private Instant dueDate;

    @NotNull
    @Column(nullable = false, name = "customer_id")
    private UUID customerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "account_status", nullable = false)
    private LoanAccountStatus accountStatus = LoanAccountStatus.ACTIVE;

    public LoanAccount() {}

    public static LoanAccount newInstance(BigDecimal repaymentAmount,
                                          BigDecimal principalAmount,
                                          UUID productId,
                                          Instant dueDate,
                                          UUID customerId,
                                          PaymentMethod paymentMethod) {

        return new LoanAccount(repaymentAmount, principalAmount, productId, dueDate, customerId, paymentMethod);
    }

    private LoanAccount(BigDecimal repaymentAmount,
                       BigDecimal principalAmount,
                       UUID productId,
                       Instant dueDate,
                       UUID customerId,
                       PaymentMethod paymentMethod) {
        this.repaymentAmount = repaymentAmount;
        this.principalAmount = principalAmount;
        this.productId = productId;
        this.dueDate = dueDate;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public LoanAccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(LoanAccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void markAsClosed() {
        this.setAccountStatus(LoanAccountStatus.CLOSED);
        this.setUpdatedAt(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoanAccount.class.getSimpleName() + "[", "]")
                .add("amount=" + repaymentAmount)
                .add("principalAmount=" + principalAmount)
                .add("productId=" + productId)
                .add("dueDate=" + dueDate)
                .add("customerId=" + customerId)
                .add("paymentMethod=" + paymentMethod)
                .add("accountStatus=" + accountStatus)
                .add("id=" + id)
                .add("createdAt=" + createdAt)
                .add("updatedAt=" + updatedAt)
                .toString();
    }

    public boolean hasDueDatePassed() {
        return Instant.now().isAfter(getDueDate());
    }

    public boolean isActive() {
        return LoanAccountStatus.ACTIVE.equals(getAccountStatus());
    }

    public boolean isClosed() {
        return LoanAccountStatus.CLOSED.equals(getAccountStatus());
    }
}
