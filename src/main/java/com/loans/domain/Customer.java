package com.loans.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.StringJoiner;

@Entity
@Table(name = "customer")
public class Customer extends AbstractEntity {

    @NotBlank
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @NotBlank
    @Column(nullable = false, name = "last_name")
    private String lastName;

    @NotBlank
    @Email(regexp = ".+[@].+[\\.].+")
    @Column(name = "email_address",  nullable = false, unique = true)
    private String emailAddress;

    @NotBlank
    @Column(name = "phone_number",  nullable = false, unique = true)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "communication_method", nullable = false)
    private CommunicationMethod communicationMethod;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private VirtualAccount virtualAccount = new VirtualAccount();

    public Customer() {}

    public static Customer newInstance(String firstName,
                                       String lastName,
                                       String emailAddress,
                                       String phoneNumber,
                                       CommunicationMethod communicationMethod) {
        return new Customer(firstName, lastName, emailAddress, phoneNumber, communicationMethod);
    }

    private Customer(String firstName,
                    String lastName,
                    String emailAddress,
                    String phoneNumber,
                    CommunicationMethod communicationMethod) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.communicationMethod = communicationMethod;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CommunicationMethod getCommunicationMethod() {
        return communicationMethod;
    }

    public void setCommunicationMethod(CommunicationMethod communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VirtualAccount getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(VirtualAccount virtualAccount) {
        this.virtualAccount = virtualAccount;
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
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("emailAddress=" + emailAddress)
                .add("phoneNumber=" + phoneNumber)
                .add("communicationMethod=" + communicationMethod)
                .add("id=" + id)
                .add("createdAt=" + createdAt)
                .add("updatedAt=" + updatedAt)
                .toString();
    }

    public void creditVirtualAccount(BigDecimal amount) {
        this.virtualAccount.addBalance(amount);
    }

    public void debitVirtualAccount(BigDecimal amount) {
        this.virtualAccount.subtractBalance(amount);
    }

    public boolean hasVirtualAccount() {
        return null != getVirtualAccount();
    }
}
