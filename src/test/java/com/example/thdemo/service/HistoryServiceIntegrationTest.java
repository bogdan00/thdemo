package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.ProductHistory;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.dto.CreateSupplierDto;
import com.example.thdemo.dto.ProductHistoryResponseDto;
import com.example.thdemo.repository.ProductHistoryRepository;
import com.example.thdemo.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static com.example.thdemo.TestUtil.FULL_IMPORT;

@SpringBootTest
class HistoryServiceIntegrationTest {

    @Autowired
    SupplierService supplierService;
    @Autowired
    ProductHistoryRepository productHistoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    HistoryService historyService;

    Supplier supplier;
    List<Product> expectedProducts;

    @BeforeEach
    void setupEach() {
        supplier = supplierService.create(CreateSupplierDto.builder()
                .name("Test supplier")
                .stockExpiryTimeoutDays(2)
                .build()
        );
        expectedProducts = supplierService.importProducts(supplier.getSupplierId().toString(), FULL_IMPORT);
    }

    @Test
    void given_products_when_product_history_then_should_return_history() {
        //given
        Product product = expectedProducts.get(0);
        List<ProductHistory> producedHistory = produceSynteticHistory(product);

        //when
        ProductHistoryResponseDto productHistory = historyService.getProductHistory(product.getProductId().toString());

        //then
        Assertions.assertNotNull(productHistory.getProduct());
        Assertions.assertEquals(producedHistory.size() + 2, productHistory.getPriceHistory().size());
        Assertions.assertEquals(producedHistory.size() + 2, productHistory.getStockHistory().size());
    }

    @Test
    void given_products_when_supplier_history_then_should_return_history() {
        //given

        //when
        List<ProductHistoryResponseDto> productHistory = historyService.getSupplierHistory(supplier.getSupplierId().toString());

        //then
        Assertions.assertEquals(expectedProducts.size(), productHistory.size());
    }


    private List<ProductHistory> produceSynteticHistory(Product product) {
        ProductHistory productHistory = productHistoryRepository.findAllByProductId(product.getProductId()).get(0);
        List<ProductHistory> history = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            history.add(productHistory.toBuilder()
                    .historyId(UUID.randomUUID())
                    .version(productHistory.getVersion() + 1 + i)
                    .fixedPriceEur(new Random().nextLong(1_00, 1000_00))
                    .quantityCbm(new Random().nextInt(1, 1000))
                    .build()
            );
        }
        return productHistoryRepository.saveAll(history);
    }

}