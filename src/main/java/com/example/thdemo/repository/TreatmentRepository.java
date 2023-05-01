package com.example.thdemo.repository;

import com.example.thdemo.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, UUID> {
}
