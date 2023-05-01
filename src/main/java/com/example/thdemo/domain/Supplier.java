package com.example.thdemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "supplier")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @Column(name = "supplier_id")
    UUID supplierId;

    @Column(name = "name")
    String name;

    @Column(name = "stock_expiry_timeout_days")
    Integer stockExpiryTimeoutDays;

    @ManyToOne
    @JoinColumn(name = "contact_person_id")
    Person contactPerson;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    Person salesRep;
}
