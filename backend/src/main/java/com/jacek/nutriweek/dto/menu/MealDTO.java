package com.jacek.nutriweek.dto.menu;

import java.util.List;

public record MealDTO (String name,
                       Long id,
                       float caloriesPercent,
                       List<MealItemDTO> mealItems){}
