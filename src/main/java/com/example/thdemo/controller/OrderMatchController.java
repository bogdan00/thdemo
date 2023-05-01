package com.example.thdemo.controller;

import com.example.thdemo.domain.Product;
import com.example.thdemo.dto.ProductOrderRequestDto;
import com.example.thdemo.service.OrderMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/order/")
@RequiredArgsConstructor
public class OrderMatchController {

    final OrderMatchService orderMatchService;

    @PostMapping("/match/stock")
    public ResponseEntity<List<Product>> matchStockRequest(@RequestBody ProductOrderRequestDto productOrderRequestDto) {
        return ResponseEntity.ok(orderMatchService.nonExpiredMatches(productOrderRequestDto));
    }

    @PostMapping("/match/any")
    public ResponseEntity<List<Product>> matchAnyRequest(@RequestBody ProductOrderRequestDto productOrderRequestDto) {
        return ResponseEntity.ok(orderMatchService.allMatches(productOrderRequestDto));
    }

}
