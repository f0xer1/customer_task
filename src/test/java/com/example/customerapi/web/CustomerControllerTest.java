package com.example.customerapi.web;

import com.example.customerapi.repository.CustomerRepository;
import com.example.customerapi.testcontainer.TestContainersConfig;
import com.example.customerapi.web.dto.CustomerCreationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ContextConfiguration(classes = TestContainersConfig.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository repository;

    @AfterEach
    public void cleanAll() {
        repository.deleteAll();
    }
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Sql("/users-create.sql")
    void shouldCreateNewCustomerWhenCustomerIsValid() throws Exception{

        var customer = getCustomer("Oleg Proms", "oleg@gmail.com", "+38009822322");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);

        var result = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(11L),
                jsonPath("$.fullName").value(customer.getFullName()),
                jsonPath("$.email").value(customer.getEmail()),
                jsonPath("$.phone").value(customer.getPhone()));
    }

    private CustomerCreationDto getCustomer(String fullName, String  email, String  phone) {
        return new CustomerCreationDto(fullName, email,
                phone);
    }
}
