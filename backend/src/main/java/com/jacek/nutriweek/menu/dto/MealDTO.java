package com.jacek.nutriweek.menu.dto;

import java.util.List;

public record MealDTO (Long id,
                       String name,
                       float caloriesPercent,
                       List<MealItemDTO> mealItems){}
