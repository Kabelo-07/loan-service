package com.loans.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loans.api.api.model.LoanOfferRequestDTO;
import com.loans.api.api.model.PaymentMethodDTO;
import com.loans.domain.CommunicationMethod;
import com.loans.domain.Customer;
import com.loans.domain.Product;
import com.loans.domain.VirtualAccount;
import com.loans.repository.CustomerRepository;
import com.loans.repository.ProductRepository;
import com.loans.service.impl.SmsNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanOffersApiDelegateImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private SmsNotificationService notificationService;


    @BeforeEach
    public void setUp() {
        Product product = new Product();
        product.setTenure(15);
        product.setName("Product One");
        product.setInterestPercentage(BigDecimal.valueOf(10));
        product.setMaxAllowedLimit(BigDecimal.valueOf(1000));

        repository.save(product);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void test_canListLoanOffers() throws Exception {
        //given loan products exists, then we can view a list of loan offers

        mockMvc.perform(get("/api/loan-offers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Product One"))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].tenure").value(15))
                .andExpect(jsonPath("$[0].interest_percentage").value(10))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    void test_customerCanAcceptLoanOffer() throws Exception {
        //given loan products exists
        assertFalse(repository.findAll().isEmpty());

        Product product = repository.findAll().get(0);

        //and a customer is created
        Customer customer = new Customer();
        customer.setVirtualAccount(new VirtualAccount());
        customer.setCommunicationMethod(CommunicationMethod.SMS);
        customer.setEmailAddress("test@test.com");
        customer.setFirstName("name");
        customer.setLastName("A");
        customer.setPhoneNumber("0111231234");
        customer = customerRepository.save(customer);

        // and customer accepts one loan product
        LoanOfferRequestDTO requestDTO = new LoanOfferRequestDTO()
                .customerId(customer.getId())
                .paymentMethod(PaymentMethodDTO.BANK_TRANSFER);

        mockMvc.perform(post("/api/loan-offers/{product-id}/accept", product.getId())
                        .content(new ObjectMapper().writeValueAsBytes(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.principal_amount").value(1000))
                .andExpect(jsonPath("$.repayment_amount").value(1100))
                .andExpect(jsonPath("$.due_date").isNotEmpty())
                .andExpect(jsonPath("$.payment_method").value(PaymentMethodDTO.BANK_TRANSFER.getValue()));

        //and the customer virtual account is credited with the loan amount
        Optional<Customer> optional = customerRepository.findById(customer.getId());
        assertTrue(optional.isPresent());
        assertNotNull(optional.get().getVirtualAccount());
        assertEquals(1000L, optional.get().getVirtualAccount()
                .getBalance().longValue());

        //and an accept notification is sent to the customer
        verify(notificationService, times(1)).send(any());
    }

    @Test
    void test_customerCannotAccept_LoanOffer_thatDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/loan-offers/{product-id}/accept", UUID.randomUUID())
                        .content(new ObjectMapper().writeValueAsBytes(new LoanOfferRequestDTO()
                                .customerId(UUID.randomUUID())
                                .paymentMethod(PaymentMethodDTO.BANK_TRANSFER)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void test_LoanOffer_cannotBeCreated_ForInvalidCustomer() throws Exception {
        //given the loan product selected does exist
        assertFalse(repository.findAll().isEmpty());

        Product product = repository.findAll().get(0);

        //and the customer provided is invalid
        mockMvc.perform(post("/api/loan-offers/{product-id}/accept", product.getId())
                        .content(new ObjectMapper().writeValueAsBytes(new LoanOfferRequestDTO()
                                .customerId(UUID.randomUUID())
                                .paymentMethod(PaymentMethodDTO.BANK_TRANSFER)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
        // then a http 422 is returned to client
    }

    @Test
    void test_BadRequestIsReturned_forIncompleteRequestData() throws Exception {
        mockMvc.perform(post("/api/loan-offers/{product-id}/accept", UUID.randomUUID())
                        .content(new ObjectMapper().writeValueAsBytes(new LoanOfferRequestDTO()
                                .customerId(UUID.randomUUID())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request sent"))
                .andExpect(jsonPath("$.errors[0]").value("paymentMethod: must not be null"));
    }

}