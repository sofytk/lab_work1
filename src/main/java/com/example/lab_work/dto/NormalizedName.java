package com.example.lab_work.dto;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record NormalizedName(

        String surname,
        String name,
        String patronymic,

        Character nameInitial,
        Character patronymicInitial,

        String originalFullName

) {

    public String getStandardRepresentation() {

        return Stream.of(
                        surname,
                        name,
                        patronymic
                )
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "));
    }

    public String getDisplayName() {

        if (surname == null) {
            return originalFullName;
        }

        if (name != null) {
            return surname + " " + name.charAt(0) + ".";
        }

        if (nameInitial != null) {
            return surname + " " + nameInitial + ".";
        }

        return surname;
    }
}
