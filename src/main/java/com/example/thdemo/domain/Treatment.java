package com.example.thdemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@Entity
@Table(name = "treatment")
@NoArgsConstructor
@AllArgsConstructor
public class Treatment {
    @Id
    @Column(name = "TREATMENT_ID")
    UUID treatmentId;

    @Column(name = "name")
    String name;
}
