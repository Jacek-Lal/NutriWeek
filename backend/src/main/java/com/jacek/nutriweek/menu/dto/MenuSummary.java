package com.jacek.nutriweek.menu.dto;

import java.time.LocalDate;

public record MenuSummary(Long id,
                          String name,
                          int days,
                          LocalDate startDate
                             ){ }
