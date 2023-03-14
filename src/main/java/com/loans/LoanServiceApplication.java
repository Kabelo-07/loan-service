package com.loans;

import com.loans.api.api.model.CommunicationMethodDTO;
import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Product;
import com.loans.repository.ProductRepository;
import com.loans.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
public class LoanServiceApplication implements CommandLineRunner {

	private final ProductRepository repository;
	private final CustomerService customerService;

	public LoanServiceApplication(ProductRepository repository, CustomerService customerService) {
		this.repository = repository;
		this.customerService = customerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LoanServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Product productA = Product.newInstance("Product A", 1000, 10, 15);
		Product productB = Product.newInstance("Product B", 2500, 12.5f, 30);

		repository.saveAll(List.of(productA, productB));

		customerService.createCustomer(new CustomerDTO()
				.communicationMethod(CommunicationMethodDTO.SMS)
				.phoneNumber("0891231234")
				.emailAddress("js@email.tech")
				.firstName("Jon")
				.lastName("Snow"));


		customerService.createCustomer(new CustomerDTO()
				.communicationMethod(CommunicationMethodDTO.EMAIL)
				.phoneNumber("8218218128")
				.emailAddress("sm@email.tech")
				.firstName("Steve")
				.lastName("Smith"));

	}
}
