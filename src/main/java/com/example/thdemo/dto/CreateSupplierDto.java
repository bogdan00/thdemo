package com.example.thdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSupplierDto {
    String name;
    Integer stockExpiryTimeoutDays;
}
