package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.dto.CreateSupplierDto;
import com.example.thdemo.dto.ProductOrderRequestDto;
import com.example.thdemo.repository.ProductHistoryRepository;
import com.example.thdemo.repository.ProductRepository;
import com.example.thdemo.repository.SupplierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static com.example.thdemo.TestUtil.FULL_IMPORT;

@SpringBootTest
class OrderMatchServiceIntegrationTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductHistoryRepository productHistoryRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierService supplierService;
    @Autowired
    ImportService importService;
    @Autowired
    OrderMatchService matchService;

    ProductOrderRequestDto orderRequestDto = ProductOrderRequestDto.builder()
            .species("Fir")
            .thickness(50)
            .width(100)
            .length(3000)
            .build();

    @BeforeEach
    void setupEach() {
        supplierRepository.deleteAll();
        productRepository.deleteAll();
        productHistoryRepository.deleteAll();
    }

    @Test
    void given_non_expired_products_when_non_expired_search_should_match() {
        //given
        Supplier supplier = supplierService.create(CreateSupplierDto.builder()
                .name("test supplier")
                .stockExpiryTimeoutDays(3)
                .build()
        );
        importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);

        //when
        List<Product> products = matchService.nonExpiredMatches(orderRequestDto);

        //then
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void given_expired_products_when_non_expired_search_should_not_match() {
        //given
        Supplier supplier = supplierService.create(CreateSupplierDto.builder()
                .name("test supplier")
                .stockExpiryTimeoutDays(-1)
                .build()
        );
        importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);

        //when
        List<Product> products = matchService.nonExpiredMatches(orderRequestDto);

        //then
        Assertions.assertEquals(0, products.size());
    }

    @Test
    void given_expired_products_when_all_matches_search_should_match() {
        //given
        Supplier supplier = supplierService.create(CreateSupplierDto.builder()
                .name("test supplier")
                .stockExpiryTimeoutDays(-1)
                .build()
        );
        importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);

        //when
        List<Product> products = matchService.allMatches(orderRequestDto);

        //then
        Assertions.assertEquals(1, products.size());
    }
}