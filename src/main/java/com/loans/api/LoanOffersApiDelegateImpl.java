package com.loans.api;

import com.loans.api.api.LoanOffersApiDelegate;
import com.loans.api.api.model.LoanAccountDTO;
import com.loans.api.api.model.LoanOfferRequestDTO;
import com.loans.api.api.model.ProductDTO;
import com.loans.domain.Customer;
import com.loans.domain.LoanAccount;
import com.loans.domain.Product;
import com.loans.exceptions.InvalidRequestException;
import com.loans.mapper.LoanAccountMapper;
import com.loans.mapper.ProductMapper;
import com.loans.service.CustomerService;
import com.loans.service.LoanAccountService;
import com.loans.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class LoanOffersApiDelegateImpl implements LoanOffersApiDelegate {

    private final ProductService productService;
    private final CustomerService customerService;
    private final LoanAccountService loanService;

    public LoanOffersApiDelegateImpl(ProductService productService,
                                     CustomerService customerService,
                                     LoanAccountService loanService) {
        this.productService = productService;
        this.customerService = customerService;
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<List<ProductDTO>> listLoanOffers() {
        List<Product> products = productService.retrieveProducts();
        if (CollectionUtils.isEmpty(products)) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ProductMapper.INSTANCE.toDto(products));
    }

    @Override
    @Transactional
    public ResponseEntity<LoanAccountDTO> acceptLoanOffer(UUID productId, LoanOfferRequestDTO dto) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new InvalidRequestException(String.format("Invalid product [%s]", productId)));

        Customer customer = customerService.findById(dto.getCustomerId())
                .orElseThrow(() -> new InvalidRequestException(String.format("Invalid customer [%s]", dto.getCustomerId())));

        LoanAccount loanAccount = loanService.createLoanAccount(product, customer.getId(), dto.getPaymentMethod());

        customerService.creditVirtualAccount(customer, loanAccount.getPrincipalAmount());

        return ResponseEntity.ok(LoanAccountMapper.INSTANCE.toDto(loanAccount));
    }

    @Override
    public ResponseEntity<Void> declineLoanOffer(UUID productId) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new InvalidRequestException(String.format("Invalid product [%s]", productId)));

        log.info("Loan offer for product {} was declined", product);

        return ResponseEntity.noContent().build();
    }

}
