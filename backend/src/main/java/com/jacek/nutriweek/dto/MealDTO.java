package com.jacek.nutriweek.dto;

import java.util.List;

public record MealDTO (String name,
                       float targetKcal,
                       float targetCarb,
                       float targetFat,
                       float targetProtein,
                       List<MealItemDTO> mealItems){}
