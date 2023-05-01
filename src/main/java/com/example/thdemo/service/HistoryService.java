package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.ProductHistory;
import com.example.thdemo.dto.ProductHistoryResponseDto;
import com.example.thdemo.dto.ProductHistoryResponseDto.DataPoint;
import com.example.thdemo.repository.ProductHistoryRepository;
import com.example.thdemo.repository.ProductRepository;
import com.example.thdemo.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class HistoryService {

    final ProductHistoryRepository productHistoryRepository;
    final ProductRepository productRepository;
    final SupplierRepository supplierRepository;

    public ProductHistoryResponseDto getProductHistory(String productId) {
        UUID productUUID = UUID.fromString(productId);
        Product product = productRepository.findById(productUUID).orElseThrow();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAllByProductId(productUUID);
        List<DataPoint<Long>> priceDataPoints = getPriceDataPoints(productHistoryList);
        List<DataPoint<Integer>> stockDataPoints = getStockDataPoints(productHistoryList);

        return ProductHistoryResponseDto.builder()
                .product(product)
                .priceHistory(priceDataPoints)
                .stockHistory(stockDataPoints)
                .build();
    }

    public List<ProductHistoryResponseDto> getSupplierHistory(String supplierId) {
        UUID supplierUUID = UUID.fromString(supplierId);

        List<ProductHistory> productHistoryList = productHistoryRepository.findAllBySupplierSupplierId(supplierUUID);
        return getProductHistoryResponseDtos(productHistoryList);
    }


    public List<ProductHistoryResponseDto> getAllGroupedHistory() {
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll();
        return getProductHistoryResponseDtos(productHistoryList);
    }

    private List<ProductHistoryResponseDto> getProductHistoryResponseDtos(List<ProductHistory> productHistoryList) {
        Map<UUID, Product> uuidProductMap = getLatestProduct(productHistoryList);

        return uuidProductMap.values().stream()
                .map(product -> {
                    List<ProductHistory> currentProductHistory = productHistoryList.stream()
                            .filter(it -> it.getProductId().equals(product.getProductId()))
                            .toList();

                    return ProductHistoryResponseDto.builder()
                            .product(product)
                            .priceHistory(getPriceDataPoints(currentProductHistory))
                            .stockHistory(getStockDataPoints(currentProductHistory))
                            .build()
                            ;
                })
                .toList();
    }

    private Map<UUID, Product> getLatestProduct(List<ProductHistory> allBySupplier) {
        return allBySupplier.stream()
                .collect(Collectors.toMap(
                        ProductHistory::getProductId,
                        this::toProduct,
                        (left, right) -> left.getVersion() > right.getVersion() ? left : right
                ));
    }

    private Product toProduct(ProductHistory it) {
        return Product.builder()
                .productId(it.getProductId())
                .supplier(it.getSupplier())
                .version(it.getVersion())

                .species(it.getSpecies())
                .gradeType(it.getGradeType())
                .grade(it.getGrade())
                .drying(it.getDrying())
                .treatment(it.getTreatment())

                .thickness(it.getThickness())
                .width(it.getWidth())
                .length(it.getLength())

                .fixedPriceEur(it.getFixedPriceEur())
                .quantityCbm(it.getQuantityCbm())

                .build();
    }

    private static List<DataPoint<Long>> getPriceDataPoints(List<ProductHistory> productHistoryList) {
        return productHistoryList.stream()
                .map(it -> new DataPoint<>(
                        it.getFixedPriceEur(),
                        it.getCreatedOn()
                ))
                .toList();
    }

    private static List<DataPoint<Integer>> getStockDataPoints(List<ProductHistory> productHistoryList) {
        return productHistoryList.stream()
                .map(it -> new DataPoint<>(
                        it.getQuantityCbm(),
                        it.getCreatedOn()
                ))
                .toList();
    }
}
