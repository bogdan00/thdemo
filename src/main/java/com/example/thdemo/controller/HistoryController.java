package com.example.thdemo.controller;

import com.example.thdemo.dto.ProductHistoryResponseDto;
import com.example.thdemo.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    final HistoryService historyService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductHistoryResponseDto> getProductHistoricalData(
            @PathVariable String productId
    ) {
        ProductHistoryResponseDto productHistory = historyService.getProductHistory(productId);
        return ResponseEntity.ok(productHistory);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductHistoryResponseDto>> geSupplierHistoricalData(
            @PathVariable String supplierId
    ) {
        List<ProductHistoryResponseDto> supplierHistory = historyService.getSupplierHistory(supplierId);
        return ResponseEntity.ok(supplierHistory);
    }

    @GetMapping("/grouped")
    public ResponseEntity<List<ProductHistoryResponseDto>> geSupplierHistoricalData() {
        List<ProductHistoryResponseDto> allGroupedHistory = historyService.getAllGroupedHistory();
        return ResponseEntity.ok(allGroupedHistory);
    }
}
