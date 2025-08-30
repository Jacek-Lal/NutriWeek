package com.jacek.nutriweek.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiProduct {
    private int fdcId;
    private String description;
    private List<ApiNutrient> foodApiNutrients;
}
