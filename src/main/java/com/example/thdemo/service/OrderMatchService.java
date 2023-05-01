package com.example.thdemo.service;

import com.example.thdemo.domain.Product;
import com.example.thdemo.dto.ProductOrderRequestDto;
import com.example.thdemo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderMatchService {

    final ProductRepository productRepository;

    public List<Product> nonExpiredMatches(ProductOrderRequestDto orderRequestDto) {
        return productRepository.findAllBySpeciesNameIgnoreCaseAndThicknessAndWidthAndLengthAndExpiresOnAfter(
                orderRequestDto.getSpecies(),
                orderRequestDto.getThickness(),
                orderRequestDto.getWidth(),
                orderRequestDto.getLength(),
                LocalDateTime.now()
        );
    }

    public List<Product> allMatches(ProductOrderRequestDto orderRequestDto) {
        return productRepository.findAllBySpeciesNameIgnoreCaseAndThicknessAndWidthAndLength(
                orderRequestDto.getSpecies(),
                orderRequestDto.getThickness(),
                orderRequestDto.getWidth(),
                orderRequestDto.getLength()
        );
    }

}
