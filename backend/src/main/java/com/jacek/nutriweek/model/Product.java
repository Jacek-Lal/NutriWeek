package com.jacek.nutriweek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int fdcId;
    @JsonProperty("description")
    private String description;
    private List<Nutrient> foodNutrients;
}
