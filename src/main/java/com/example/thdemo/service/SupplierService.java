package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.dto.CreateSupplierDto;
import com.example.thdemo.repository.ProductRepository;
import com.example.thdemo.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SupplierService {

    final ProductRepository productRepository;
    final SupplierRepository supplierRepository;
    final ImportService importService;


    public Supplier create(CreateSupplierDto createSupplierDto) {
        String supplierName = Objects.requireNonNull(createSupplierDto.getName());
        Integer stockExpiryTimeoutDays = Objects.requireNonNull(createSupplierDto.getStockExpiryTimeoutDays());

        Supplier toSave = Supplier.builder()
                .supplierId(UUID.randomUUID())
                .name(supplierName)
                .stockExpiryTimeoutDays(stockExpiryTimeoutDays)
                .build();

        return supplierRepository.save(toSave);
    }

    public List<Product> importProducts(String supplierId, String requestBody) {
        UUID supplierUUID = UUID.fromString(supplierId);

        return importService.importProducts(supplierUUID, requestBody);
    }

    public List<Product> getSupplierProducts(String supplierId) {
        Supplier supplier = supplierRepository.findById(UUID.fromString(supplierId)).orElseThrow();

        return productRepository.findAllBySupplier(supplier);
    }
}
