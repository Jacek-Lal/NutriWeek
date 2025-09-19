package com.jacek.nutriweek.dto;

import java.util.List;

public record MenuDTO(String name,
                      int days,
                      int meals,
                      int calories,
                      String startDate,
                      int targetFat,
                      int targetProtein,
                      int targetCarb,
                      List<Integer> caloriesPerMeal){
}
