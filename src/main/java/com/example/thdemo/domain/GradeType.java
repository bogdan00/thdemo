package com.example.thdemo.domain;

import java.util.*;

public enum GradeType {
    TEGERNSEER("Tegernseer", List.of("O", "I", "II", "III", "IV", "V")),
    NORDIC_BLUE("Nordic Blue", List.of("A1", "A2", "A3", "A4", "B"));

    public final String gradeName;
    public final List<String> validGrades;

    GradeType(String gradeName, List<String> validGrades) {
        this.gradeName = gradeName;
        this.validGrades = validGrades;
    }

    public static GradeType of(String name) {
        return Arrays.stream(GradeType.values())
                .filter(it -> it.gradeName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find " + name));
    }
}
