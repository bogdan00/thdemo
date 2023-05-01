package com.example.thdemo.service;

import com.example.thdemo.domain.Drying;
import com.example.thdemo.domain.GradeType;
import com.example.thdemo.domain.Product;
import com.example.thdemo.domain.ProductHistory;
import com.example.thdemo.domain.Species;
import com.example.thdemo.domain.Supplier;
import com.example.thdemo.domain.Treatment;
import com.example.thdemo.dto.ImportProductDto;
import com.example.thdemo.repository.DryingMethodRepository;
import com.example.thdemo.repository.ProductHistoryRepository;
import com.example.thdemo.repository.ProductRepository;
import com.example.thdemo.repository.ProductSpeciesRepository;
import com.example.thdemo.repository.SupplierRepository;
import com.example.thdemo.repository.TreatmentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportService {
    final SupplierRepository supplierRepository;
    final ProductRepository productRepository;
    final ProductHistoryRepository productHistoryRepository;
    final DryingMethodRepository dryingMethodRepository;
    final ProductSpeciesRepository productSpeciesRepository;
    final TreatmentRepository treatmentRepository;

    public List<Product> importProducts(UUID supplierId, String toImport) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();

        ImportContext importContext = getImportContext(supplier);

        return Arrays.stream(toImport.split("\n"))
                .map(this::toImportProduct)
                .map(importProductDto -> toProduct(importProductDto, importContext))
                .map(this::matchProduct)
                .map(matched -> {
                    Product newProduct = matched.getSecond();
                    Product toSave = matched.getFirst()
                            .map(existingProduct -> existingProduct.toBuilder()
                                    .version(existingProduct.getVersion() + 1)
                                    .quantityCbm(newProduct.getQuantityCbm())
                                    .fixedPriceEur(newProduct.getFixedPriceEur())
                                    .build()
                            )
                            .orElse(newProduct);
                    productHistoryRepository.save(toProductHistory(toSave));
                    return productRepository.save(toSave);
                })
                .toList();

    }

    private Pair<Optional<Product>, Product> matchProduct(Product newProduct) {
        Optional<Product> match = this.productRepository.findMatches(newProduct);
        return Pair.of(match, newProduct);
    }

    private ImportContext getImportContext(Supplier supplier) {
        return ImportContext.builder()
                .supplier(supplier)
                .drying(dryingMethodRepository.findAll())
                .species(productSpeciesRepository.findAll())
                .treatment(treatmentRepository.findAll())
                .now(LocalDateTime.now())
                .build();
    }

    ImportProductDto toImportProduct(String importProductString) {
        // Spruce, Nordic Blue/A1, Fresh, Heat Treated - 50x150x1200
        Objects.requireNonNull(importProductString);

        List<String> dataAndSize = splitTrim(importProductString, "-");
        Assert.isTrue(dataAndSize.size() == 2, "Must have size and information per line: [%s]".formatted(importProductString));

        List<String> columns = splitTrim(dataAndSize.get(0), ",");

        Pair<GradeType, String> typeAndGrade = getTypeAndGrade(columns.get(1), importProductString);
        List<Integer> dimensions = getDimensions(dataAndSize.get(1), importProductString);

        String drying = columns.size() > 2 ? columns.get(2) : null;
        String treatment = columns.size() > 3 ? columns.get(3) : null;

        return ImportProductDto.builder()
                .species(columns.get(0))
                .gradeType(typeAndGrade.getFirst())
                .grade(typeAndGrade.getSecond())
                .drying(drying)
                .treatment(treatment)
                .thickness(dimensions.get(0))
                .width(dimensions.get(1))
                .length(dimensions.get(2))
                .build();
    }

    private Pair<GradeType, String> getTypeAndGrade(String column, String importProductString) {
        List<String> gradeJoined = splitTrim(column, "/");
        GradeType gradeType = GradeType.of(gradeJoined.get(0));
        String grade = gradeJoined.get(1);
        Assert.isTrue(
                gradeType.validGrades.contains(grade),
                "Invalid grade provided: %s in product: [%s]".formatted(
                        grade, importProductString
                ));
        return Pair.of(gradeType, grade);
    }

    private List<Integer> getDimensions(String dimension, String importProductString) {

        int thickness;
        int width;
        int length;
        try {
            String[] dimensionParts = dimension.split("x");
            thickness = Integer.parseInt(dimensionParts[0]);
            width = Integer.parseInt(dimensionParts[1]);
            length = Integer.parseInt(dimensionParts[2]);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Can't parse dimension %s in product: [%s]".formatted(
                    dimension, importProductString
            ));
        }
        return List.of(thickness, width, length);
    }

    Product toProduct(ImportProductDto importProductDto, ImportContext importContext) {
        Species species = importContext.species.stream()
                .filter(it -> it.getName().equalsIgnoreCase(importProductDto.getSpecies()))
                .findFirst()
                .orElse(null);

        Drying drying = importContext.drying.stream()
                .filter(it -> it.getName().equalsIgnoreCase(importProductDto.getDrying()))
                .findFirst()
                .orElse(null);

        Treatment treatment = importContext.treatment.stream()
                .filter(it -> it.getName().equalsIgnoreCase(importProductDto.getTreatment()))
                .findFirst()
                .orElse(null);

        LocalDateTime expiresOn = importContext.now.plusDays(importContext.supplier.getStockExpiryTimeoutDays());

        return Product.builder()
                .productId(UUID.randomUUID())
                .supplier(importContext.supplier)
                .version(0)
                .expiresOn(expiresOn)

                .species(species)
                .gradeType(importProductDto.getGradeType())
                .grade(importProductDto.getGrade())
                .drying(drying)
                .treatment(treatment)

                .thickness(importProductDto.getThickness())
                .width(importProductDto.getWidth())
                .length(importProductDto.getLength())

                //mock data
                .fixedPriceEur(new Random().nextLong(1_00, 1000_00))
                .quantityCbm(new Random().nextInt(1, 1000))

                .build();
    }

    static ProductHistory toProductHistory(Product product) {
        return ProductHistory.builder()
                .historyId(UUID.randomUUID())
                .version(product.getVersion())
                .supplier(product.getSupplier())

                .productId(product.getProductId())
                .species(product.getSpecies())
                .gradeType(product.getGradeType())
                .grade(product.getGrade())
                .drying(product.getDrying())
                .treatment(product.getTreatment())

                .thickness(product.getThickness())
                .width(product.getWidth())
                .length(product.getLength())

                .fixedPriceEur(product.getFixedPriceEur())
                .quantityCbm(product.getQuantityCbm())

                .build();
    }

    private static List<String> splitTrim(String importProductString, String separator) {
        return Arrays.stream(importProductString.split(separator))
                .map(String::trim)
                .toList();
    }


    @Builder
    record ImportContext(
            Supplier supplier,
            List<Drying> drying,
            List<Species> species,
            List<Treatment> treatment,
            LocalDateTime now
    ) {
    }

}
