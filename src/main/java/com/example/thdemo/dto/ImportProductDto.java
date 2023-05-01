package com.example.thdemo.dto;

import com.example.thdemo.domain.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportProductDto {

    Integer quantityCbm;
    Integer fixedPriceEur;

    Integer thickness;
    Integer width;
    Integer length;

    GradeType gradeType;
    String grade;
    String species;
    String drying;
    String treatment;

}
