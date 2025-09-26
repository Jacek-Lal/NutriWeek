package com.jacek.nutriweek.dto.menu;

public record MenuSummary(Long id,
                          String name,
                          int calories,
                          int days,
                          String startDate
                             ){ }
