package com.example.customerapi.web.controller;

import com.example.customerapi.service.CustomerService;
import com.example.customerapi.web.dto.CustomerCreationDto;
import com.example.customerapi.web.dto.CustomerDto;
import com.example.customerapi.web.dto.CustomerUpdateDto;
import com.example.customerapi.web.mapper.CustomerMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;
    private final CustomerMapper mapper;

    @PostMapping
    public ResponseEntity<CustomerDto> add(@RequestBody @Valid CustomerCreationDto creationDto) {
        var newCustomer = service.add(mapper.toEntity(creationDto));
        return new ResponseEntity<>(mapper.toPayload(newCustomer), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        return ResponseEntity.ok(service.findAll()
                .stream()
                .map(mapper::toPayload)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById (@PathVariable Long id){
        return ResponseEntity.of(service.findById(id).map(mapper::toPayload));
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<CustomerDto> update(@RequestBody @Valid CustomerUpdateDto updateDto, @PathVariable Long id) {
        return ResponseEntity.of(service.findById(id)
                .map(customer -> mapper.update(updateDto, customer))
                .map(service::update)
                .map(mapper::toPayload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
