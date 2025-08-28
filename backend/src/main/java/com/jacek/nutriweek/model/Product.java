package com.jacek.nutriweek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    private int fdcId;
    private String description;
    private List<Nutrient> foodNutrients;
}
