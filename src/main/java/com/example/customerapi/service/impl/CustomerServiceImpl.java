package com.example.customerapi.service.impl;

import com.example.customerapi.model.Customer;
import com.example.customerapi.repository.CustomerRepository;
import com.example.customerapi.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;


    @Override
    public Customer add(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Customer update(Customer customer) {
        customer.setUpdated(Instant.now().getEpochSecond());
        return repository.save(customer);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id).filter(Customer::getIsActive).ifPresent(customer -> customer.setIsActive(false));
    }


}
