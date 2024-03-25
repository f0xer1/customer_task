package com.example.customerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    final private Long created = Instant.now().getEpochSecond();
    private Long updated;
    private String email;
    private String fullName;
    private String phone;
    private Boolean isActive = true;
}
