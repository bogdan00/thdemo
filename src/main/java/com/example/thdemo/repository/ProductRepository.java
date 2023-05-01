package com.example.thdemo.repository;

import com.example.thdemo.domain.Drying;
import com.example.thdemo.domain.GradeType;
import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.Species;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    default Optional<Product> findMatches(Product product) {
        return findFirstByThicknessAndWidthAndLengthAndGradeTypeAndGradeAndSpeciesAndDryingAndTreatment(
                product.getThickness(),
                product.getWidth(),
                product.getLength(),
                product.getGradeType(),
                product.getGrade(),
                product.getSpecies(),
                product.getDrying(),
                product.getTreatment()
        );
    }

    Optional<Product> findFirstByThicknessAndWidthAndLengthAndGradeTypeAndGradeAndSpeciesAndDryingAndTreatment(
            Integer thickness,
            Integer width,
            Integer length,
            GradeType gradeType,
            String grade,
            Species species,
            Drying drying,
            Treatment treatment
    );

    List<Product> findAllBySupplier(Supplier supplier);

    List<Product> findAllBySpeciesNameIgnoreCaseAndThicknessAndWidthAndLength(
            String species,
            Integer thickness,
            Integer width,
            Integer length
    );

    List<Product> findAllBySpeciesNameIgnoreCaseAndThicknessAndWidthAndLengthAndExpiresOnAfter(
            String species,
            Integer thickness,
            Integer width,
            Integer length,
            LocalDateTime expiresOn
    );

}
