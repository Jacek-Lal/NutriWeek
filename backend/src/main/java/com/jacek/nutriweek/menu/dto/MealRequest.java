package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MealRequest(String name,
                          int targetKcal,
                          LocalDate date) {
}
