package com.jacek.nutriweek.menu.dto.usda;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiNutrient {

    @JsonAlias("nutrientId")
    @JsonProperty("id")
    private int id;

    @JsonAlias("nutrientName")
    @JsonProperty("name")
    private String name;

    @JsonAlias("unitName")
    @JsonProperty("unit")
    private String unit;

    private double value;
    private int rank;
}
