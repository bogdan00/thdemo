package com.example.thdemo.controller;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.dto.CreateSupplierDto;
import com.example.thdemo.repository.SupplierRepository;
import com.example.thdemo.service.SupplierService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    final SupplierRepository supplierRepository;
    final SupplierService supplierService;


    @GetMapping()
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        return ResponseEntity.ok(suppliers);
    }

    @PostMapping()
    public ResponseEntity<Supplier> createSupplier(
            @RequestBody CreateSupplierDto createSupplierDto
    ) {
        Supplier supplier = supplierService.create(createSupplierDto);

        return ResponseEntity.ok(supplier);
    }

    @PostMapping("/{supplierId}/products")
    public ResponseEntity<List<Product>> importProducts(
            @PathVariable String supplierId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {@ExampleObject(
                            name = "Import sample",
                            description = "Note that newlines are used between imports",
                            value = """
                                    Spruce, Nordic Blue/A1, Fresh, Heat Treated - 50x150x1200
                                    Spruce, Nordic Blue/A1, Fresh, Heat Treated - 50x120x1200
                                    """
                    )}))
            @RequestBody String requestBody
    ) {
        List<Product> imported = supplierService.importProducts(supplierId, requestBody);
        return ResponseEntity.ok(imported);
    }

    @GetMapping("/{supplierId}/products")
    public ResponseEntity<List<Product>> getSupplierProducts(
            @PathVariable String supplierId
    ) {
        List<Product> supplierProducts = supplierService.getSupplierProducts(supplierId);
        return ResponseEntity.ok(supplierProducts);
    }
}
