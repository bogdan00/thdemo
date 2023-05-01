package com.example.thdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductOrderRequestDto {
    String species;
    Integer thickness;
    Integer width;
    Integer length;


}
