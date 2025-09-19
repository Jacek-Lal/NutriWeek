package com.jacek.nutriweek.dto;

import java.util.List;

public record MealDTO (String name,
                       Long menuId,
                       float targetCarb,
                       float targetFat,
                       float targetProtein,
                       List<MealItemDTO> mealItems){}
