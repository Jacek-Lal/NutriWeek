package com.jacek.nutriweek.dto.menu;

import java.util.List;

public record MenuRequest(String name,
                          int days,
                          int meals,
                          int calories,
                          String startDate,
                          int targetFat,
                          int targetProtein,
                          int targetCarb,
                          List<Integer> caloriesPerMeal){
}
