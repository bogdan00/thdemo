package com.example.thdemo.repository;

import com.example.thdemo.domain.Drying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DryingMethodRepository extends JpaRepository<Drying, UUID> {
}
