package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MealFlatDTO(Long mealId, LocalDate mealDate, String mealName, Integer targetKcal, Double productAmount,
                          String productName, Integer fdcId, String nutrientName, String nutrientUnit,
                          Double nutrientAmount) {

    public boolean containProducts() {
        return (this.productAmount != null && this.productName != null && this.fdcId != null);
    }

    public boolean containNutrients(){
        return (this.nutrientName != null && this.nutrientUnit != null && this.nutrientAmount != null);
    }
}
