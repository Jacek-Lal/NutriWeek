package com.jacek.nutriweek.dto;

public record MenuSummaryDTO(Long id,
                             String name,
                             int calories,
                             int days,
                             String startDate
                             ){ }
