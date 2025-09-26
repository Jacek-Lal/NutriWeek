package com.jacek.nutriweek.menu.dto;

import java.util.List;

public record MenuResponse(long id,
                           String name,
                           int days,
                           int meals,
                           int calories,
                           String startDate,
                           float targetProtein,
                           float targetFat,
                           float targetCarb,
                           List<MealDTO> mealList) {
}
