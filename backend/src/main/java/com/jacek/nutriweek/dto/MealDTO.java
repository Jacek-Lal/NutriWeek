package com.jacek.nutriweek.dto;

import java.util.List;

public record MealDTO (String name,
                       Long id,
                       float caloriesPercent,
                       List<MealItemDTO> mealItems){}
