package com.jacek.nutriweek.menu.dto;

public record MenuSummary(Long id,
                          String name,
                          int calories,
                          int days,
                          String startDate
                             ){ }
