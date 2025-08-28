package com.jacek.nutriweek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Product {
    private int fdcId;
    private String description;
    private List<Nutrient> foodNutrients;
}
