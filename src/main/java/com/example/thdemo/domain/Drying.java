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
@Table(name = "drying_method")
@NoArgsConstructor
@AllArgsConstructor
public class Drying {
    @Id
    @Column(name = "drying_method_id")
    UUID dryingMethodId;

    @Column(name = "name")
    String name;
}
