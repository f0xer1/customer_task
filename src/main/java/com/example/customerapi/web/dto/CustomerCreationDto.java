package com.example.customerapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerCreationDto {
    @NotBlank(message = "Specify full name")
    @Size(min = 2, max = 50, message = "Full name should be between 2 and 50 characters, including whitespaces")
    private String fullName;

    @NotBlank(message = "Specify email")
    @Email(message = "Enter a valid email address")
    @Size(min = 2, max = 100, message = "Email should be between 2 and 100 characters")
    private String email;

    @Size(min = 6, max = 14, message = "Phone number should be between 6 and 14 digits")
    @Pattern(regexp = "^\\+[0-9]+$", message = "Phone number should start with '+', followed by digits")
    private String phone;
}
