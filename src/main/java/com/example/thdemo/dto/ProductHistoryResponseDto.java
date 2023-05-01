package com.example.thdemo.dto;

import com.example.thdemo.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;


@Builder
@Data
public class ProductHistoryResponseDto {
    Product product;
    List<DataPoint<Long>> priceHistory;
    List<DataPoint<Integer>> stockHistory;


    @AllArgsConstructor
    public static class DataPoint<T> {
        T value;
        LocalDateTime on;
    }
}
