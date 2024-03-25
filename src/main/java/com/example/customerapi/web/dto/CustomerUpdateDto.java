package com.example.customerapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerUpdateDto {

    @NotBlank(message = "Specify full name")
    @Size(min = 2, max = 50, message = "Full name should be between 2 and 50 characters, including whitespaces")
    private String fullName;

    @Size(min = 6, max = 14, message = "Phone number should be between 6 and 14 digits")
    @Pattern(regexp = "^\\+[0-9]+$", message = "Phone number should start with '+', followed by digits")
    private String phone;
}
