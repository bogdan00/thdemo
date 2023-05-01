package com.example.thdemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;


@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "product_history")
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistory {

    @Id
    @Column(name = "history_id")
    UUID historyId;

    @Column(name = "product_id")
    UUID productId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Column(name = "version")
    Integer version;

    @Column(name = "created_on")
    @CreationTimestamp
    LocalDateTime createdOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    LocalDateTime updatedOn;

    @Column(name = "expires_on")
    LocalDateTime expiresOn;

    @Column(name = "quantity_cbm")
    Integer quantityCbm;

    @Column(name = "fixed_price_eur")
    Long fixedPriceEur;

    @Column(name = "negotiated_price_eur")
    Long negotiatedPriceEur;

    @Column(name = "thickness")
    Integer thickness;

    @Column(name = "width")
    Integer width;

    @Column(name = "length")
    Integer length;

    @Column(name = "grade_type")
    @Enumerated(EnumType.STRING)
    GradeType gradeType;

    @Column(name = "grade")
    String grade;

    @ManyToOne
    @JoinColumn(name = "product_species_id")
    Species species;

    @ManyToOne
    @JoinColumn(name = "drying_method_id")
    Drying drying;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    Treatment treatment;
}
