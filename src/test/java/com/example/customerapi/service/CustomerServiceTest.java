package com.example.customerapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import com.example.customerapi.model.Customer;
import com.example.customerapi.repository.CustomerRepository;
import com.example.customerapi.testcontainer.TestContainersConfig;
import com.example.customerapi.web.dto.CustomerUpdateDto;
import com.example.customerapi.web.mapper.CustomerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfig.class)
@Transactional
public class CustomerServiceTest {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerService service;
    @Autowired
    private  CustomerMapper mapper;


    @AfterEach
    void cleanAll() {
        repository.deleteAll();
    }

    @Test
    @Sql("/users-create.sql")
    void findById(){
        var id = 1L;
        var customer = service.findById(id);
        assertThat(customer).isPresent();
        assertEquals(customer, repository.findById(id));
    }
    @Test
    @Sql("/users-create.sql")
    void findByIfNoExist(){
        var id = 11L;
        var customer = service.findById(id);
        assertThat(customer).isEmpty();
    }
    @Test
    @Sql("/users-create.sql")
    void existsById(){
        var id = 1L;
        var customer = service.existsById(id);
        assertTrue(customer);
    }
    @Test
    @Sql("/users-create.sql")
    void existsByIdIfNoExist(){
        var id = 11L;
        var customer = service.existsById(id);
        assertFalse(customer);
    }
    @Test
    @Sql("/users-create.sql")
    void deleteById(){
        var id = 9L;
        service.deleteById(id);
        assertFalse(repository.findById(id).orElseThrow().getIsActive());
    }

    @Test
    @Sql("/users-create.sql")
    void findAll(){
        var customers = service.findAll();
        assertThat(customers).asList().size().isEqualTo(10);

    }
    @Test
    void add(){
        var customer = getCustomer();
        var customerSaved = service.add(customer);
        assertEquals(customerSaved, repository.findById(customer.getId()).orElseThrow());
    }
    @Test
    @Sql("/users-create.sql")
    void update(){
        var id = 1L;
        var updateDto = getCustomerUpdateDto();
        var customerUpdated = service.findById(id)
                .map(customer -> mapper.update(updateDto, customer))
                .map(service::update).orElseThrow();
        assertEquals(updateDto.getFullName(), customerUpdated.getFullName());
        assertEquals(updateDto.getPhone(), customerUpdated.getPhone());

    }



    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setFullName("Oleg Proms");
        customer.setEmail("oleg@gmail.com");
        customer.setPhone("+38009822322");
        return customer;
    }

    private CustomerUpdateDto getCustomerUpdateDto() {
        return new CustomerUpdateDto("Anna Guy",
                "+38009723328");
    }
}
