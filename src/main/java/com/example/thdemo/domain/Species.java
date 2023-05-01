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
@Table(name = "product_species")
@NoArgsConstructor
@AllArgsConstructor
public class Species {
    @Id
    @Column(name = "product_species_id")
    UUID speciesId;

    @Column(name = "name")
    String name;
}
