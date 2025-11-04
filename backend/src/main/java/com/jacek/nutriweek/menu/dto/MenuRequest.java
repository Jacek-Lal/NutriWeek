package com.jacek.nutriweek.menu.dto;

import jakarta.validation.constraints.*;

import java.util.List;


public record MenuRequest(

        @NotBlank
        String name,

        @Min(1) @Max(365)
        int days,

        @Min(1) @Max(10)
        int meals,

        @Min(500) @Max(10000)
        int calories,

        @NotBlank
        String startDate,

        @Min(0) @Max(100)
        int targetFat,

        @Min(0) @Max(100)
        int targetProtein,

        @Min(0) @Max(100)
        int targetCarb,

        @NotNull
        @Size(min = 1)
        List<@Min(0) @Max(100) Integer> caloriesPerMeal
) {}

