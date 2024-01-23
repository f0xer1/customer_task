package com.example.customerapi.web.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
}
