package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;
import java.util.List;

public record MealsByDate(LocalDate date, List<MealDTO> meals) {
}
