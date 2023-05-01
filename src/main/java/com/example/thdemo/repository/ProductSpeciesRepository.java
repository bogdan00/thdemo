package com.example.thdemo.repository;

import com.example.thdemo.domain.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductSpeciesRepository extends JpaRepository<Species, UUID> {
}
