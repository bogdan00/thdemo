package com.example.thdemo.service;

import com.example.thdemo.domain.Drying;
import com.example.thdemo.domain.GradeType;
import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Species;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.domain.Treatment;
import com.example.thdemo.dto.ImportProductDto;
import com.example.thdemo.service.ImportService.ImportContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ImportServiceTest {


    @InjectMocks
    ImportService importService;


    @Test
    void given_import_string_should_build_import_product() {

        ImportProductDto expected = ImportProductDto.builder()
                .species("Spruce")
                .gradeType(GradeType.NORDIC_BLUE)
                .grade("A1")
                .drying("Fresh")
                .treatment("Heat Treated")
                .thickness(50)
                .width(150)
                .length(1200)
                .build();
        ImportProductDto actual = importService.toImportProduct("Spruce, Nordic Blue/A1, Fresh, Heat Treated - 50x150x1200");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void given_import_product_and_no_matches_should_build_product() {
        ImportContext importContext = new ImportContext(
                Supplier.builder()
                        .stockExpiryTimeoutDays(2)
                        .build(),
                List.of(),
                List.of(),
                List.of(),
                LocalDateTime.now()
        );

        ImportProductDto input = ImportProductDto.builder()
                .species("Spruce")
                .gradeType(GradeType.NORDIC_BLUE)
                .grade("A1")
                .drying("Fresh")
                .treatment("Heat Treated")
                .thickness(50)
                .width(150)
                .length(1200)
                .build();

        Product expected = Product.builder()
                .supplier(importContext.supplier())
                .gradeType(GradeType.NORDIC_BLUE)
                .grade("A1")
                .species(null)
                .drying(null)
                .treatment(null)
                .thickness(50)
                .width(150)
                .length(1200)
                .build();

        Product actual = importService.toProduct(input, importContext);


        Assertions.assertEquals(expected.toExport(), actual.toExport());
        Assertions.assertEquals(importContext.now().plusDays(importContext.supplier().getStockExpiryTimeoutDays()), actual.getExpiresOn());
    }


    @Test
    void given_import_product_and_matches_should_build_product() {
        ImportContext importContext = getImportContext();

        ImportProductDto input = ImportProductDto.builder()
                .species("Spruce")
                .gradeType(GradeType.NORDIC_BLUE)
                .grade("A1")
                .drying("Fresh")
                .treatment("Heat Treated")
                .thickness(50)
                .width(150)
                .length(1200)
                .build();

        Product expected = Product.builder()
                .supplier(importContext.supplier())
                .gradeType(GradeType.NORDIC_BLUE)
                .grade("A1")
                .species(importContext.species().get(0))
                .drying(importContext.drying().get(0))
                .treatment(importContext.treatment().get(0))
                .thickness(50)
                .width(150)
                .length(1200)
                .build();

        Product actual = importService.toProduct(input, importContext);


        Assertions.assertEquals(expected.toExport(), actual.toExport());
    }

    private static ImportContext getImportContext() {
        return new ImportContext(
                new Supplier(),
                List.of(
                        new Drying(UUID.randomUUID(), "Fresh")
                ),
                List.of(
                        new Species(UUID.randomUUID(), "Spruce")
                ),
                List.of(
                        new Treatment(UUID.randomUUID(), "Heat Treated")
                ),
                LocalDateTime.now()
        );
    }
}