package com.jacek.nutriweek.dto;

import java.util.List;

public record MenuResponseDTO(long id,
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
