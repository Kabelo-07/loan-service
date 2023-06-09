package com.loans.repository;

import com.loans.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT p FROM Product p WHERE p.isActive IS TRUE")
    List<Product> findAllActiveProducts();
}
