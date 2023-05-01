package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.repository.ProductRepository;
import com.example.thdemo.repository.SupplierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static com.example.thdemo.TestUtil.FULL_IMPORT;

@SpringBootTest
class ImportServiceIntegrationTest {

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    ImportService importService;
    @Autowired
    ProductRepository productRepository;


    @Test
    void given_import_string_can_import_products() {
        // given
        Supplier supplier = Supplier.builder()
                .supplierId(UUID.randomUUID())
                .name("test supplier")
                .build();
        supplierRepository.save(supplier);

        //when
        importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);
        importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);

        //then
        List<Product> allBySupplier = productRepository.findAllBySupplier(supplier);

        Assertions.assertEquals(6, allBySupplier.size());
        Assertions.assertEquals(1, allBySupplier.get(0).getVersion());
    }


    @Test
    void given_imported_can_match_product() {
        // given
        Supplier supplier = Supplier.builder()
                .supplierId(UUID.randomUUID())
                .name("test supplier")
                .build();
        supplierRepository.save(supplier);
        List<Product> importedProducts = importService.importProducts(supplier.getSupplierId(), FULL_IMPORT);

        for (Product importedProduct : importedProducts) {
            //when
            Product matched = productRepository.findMatches(importedProduct).orElseThrow();

            //then
            Assertions.assertEquals(importedProduct, matched);
        }

    }
}