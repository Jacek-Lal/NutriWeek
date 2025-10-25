package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MealFlatDTO(Long mealId, LocalDate mealDate, String mealName, Integer targetKcal, Double productAmount,
                          String productName, Integer fdcId, String nutrientName, String nutrientUnit,
                          Double nutrientAmount) {

    public boolean isMealEmpty() {
        return (this.productAmount == null || this.productName == null || this.fdcId == null ||
                this.nutrientName == null || this.nutrientAmount == null || this.nutrientUnit == null);
    }
}
