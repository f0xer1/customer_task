package com.example.customerapi.service;

import com.example.customerapi.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer add(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    Customer update(Customer customer);

    boolean existsById(Long id);

    void deleteById(Long id);
}
