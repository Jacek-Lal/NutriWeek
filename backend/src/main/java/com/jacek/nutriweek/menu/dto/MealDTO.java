package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;
import java.util.List;

public record MealDTO (Long id,
                       String name,
                       int targetKcal,
                       LocalDate date,
                       List<MealItemDTO> mealItems){}
