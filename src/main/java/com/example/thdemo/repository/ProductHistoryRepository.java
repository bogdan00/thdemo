package com.example.thdemo.repository;

import com.example.thdemo.domain.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, UUID> {


    List<ProductHistory> findAllByProductId(UUID productId);

    List<ProductHistory> findAllBySupplierSupplierId(UUID supplierID);
}
