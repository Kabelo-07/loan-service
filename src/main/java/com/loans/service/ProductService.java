package com.loans.service;

import com.loans.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {

    List<Product> retrieveProducts();

    Optional<Product> findById(UUID productId);
}
