package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MenuResponse(long id,
                           String name,
                           int days,
                           int meals,
                           LocalDate startDate,
                           float targetProtein,
                           float targetFat,
                           float targetCarb){
}
