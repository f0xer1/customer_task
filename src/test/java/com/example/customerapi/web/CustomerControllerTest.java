package com.example.customerapi.web;

import com.example.customerapi.repository.CustomerRepository;
import com.example.customerapi.testcontainer.TestContainersConfig;
import com.example.customerapi.web.dto.CustomerCreationDto;
import com.example.customerapi.web.dto.CustomerUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void shouldCreateNewCustomerWhenCustomerIsValid() throws Exception {

        var customer = getCustomer("Oleg Proms", "oleg@gmail.com", "+38009822322");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);
        var result = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(1L),
                jsonPath("$.fullName").value(customer.getFullName()),
                jsonPath("$.email").value(customer.getEmail()),
                jsonPath("$.phone").value(customer.getPhone()));
    }

    @Test
    @Sql("/users-create.sql")
    void shouldCreateNewCustomerWhenCustomerWithNotAllowedFullName() throws Exception {
        var customer = getCustomer("-", "oleg@gmail.com", "+38009822322");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);
        var result = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Full name should be between 2 and 50 characters," +
                        " including whitespaces"));

    }

    @Test
    @Sql("/users-create.sql")
    void shouldCreateNewCustomerWhenCustomerWithNotAllowedEmail() throws Exception {
        var customer = getCustomer("Oleg Proms", "-", "+38009822322");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);
        var result = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Email should be between 2 and 100 characters," +
                        " Enter a valid email address"));

    }

    @Test
    @Sql("/users-create.sql")
    void shouldCreateNewCustomerWhenCustomerWithNotAllowedPhone() throws Exception {
        var customer = getCustomer("Oleg Proms", "oleg@gmail.com", "-");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);
        var result = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Phone number should be between 6 and 14 digits, " +
                        "Phone number should start with '+', followed by digits"));
    }


    @Test
    @Sql("/users-create.sql")
    void shouldFindAllCustomers() throws Exception {
        var result = mockMvc.perform(get("/api/customers"));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray(),
                jsonPath("$", hasSize(10))
        );

    }

    @Test
    @Sql("/users-create.sql")
    void shouldFindCustomerByIdIfExist() throws Exception {
        var id = 1L;

        var result = mockMvc.perform(get(("/api/customers/{id}"), id));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(1L));
    }

    @Test
    @Sql("/users-create.sql")
    void shouldDeleteCustomerById() throws Exception {
        var id = 1L;
        var deletedUser = repository.findById(id);

        var result = mockMvc.perform(delete("/api/customers/{id}", id));

        result.andExpectAll(status().isNoContent());
        Assertions.assertThat(deletedUser.orElseThrow().getIsActive()).isFalse();

    }

    @Test
    @Sql("/users-create.sql")
    void shouldUpdateCustomer() throws Exception {
        var id = 1L;
        var updatedUser = repository.findById(id);
        var customer = getCustomer("Anna Guy", "+38009723328");
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);
        var result = mockMvc.perform(patch("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.fullName").value(customer.getFullName()),
                jsonPath("$.phone").value(customer.getPhone()));
        Assertions.assertThat(updatedUser.orElseThrow().getUpdated()).isNotNull();

    }

    @Test
    @Sql("/users-create.sql")
    void shouldUpdateCustomerWithNotAllowedFirstName() throws Exception {
        var id = 1L;
        var customer = getCustomer("-", "+38009723328");

        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);

        var result = mockMvc.perform(patch("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Full name should be between 2 and 50 characters," +
                        " including whitespaces"));

    }

    @Test
    @Sql("/users-create.sql")
    void shouldUpdateCustomerWithNotAllowedPhone() throws Exception {
        var id = 1L;
        var customer = getCustomer("Anna Guy", "-");

        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(customer);

        var result = mockMvc.perform(patch("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Phone number should be between 6 and 14 digits, " +
                        "Phone number should start with '+', followed by digits"));

    }

    private CustomerCreationDto getCustomer(String fullName, String email, String phone) {
        return new CustomerCreationDto(fullName, email,
                phone);
    }

    private CustomerUpdateDto getCustomer(String fullName, String phone) {
        return new CustomerUpdateDto(fullName,
                phone);
    }
}
