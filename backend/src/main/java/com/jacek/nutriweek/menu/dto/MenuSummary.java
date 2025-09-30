package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MenuSummary(Long id,
                          String name,
                          int calories,
                          int days,
                          LocalDate startDate
                             ){ }
