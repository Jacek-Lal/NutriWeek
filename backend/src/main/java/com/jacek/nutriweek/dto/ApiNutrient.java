package com.jacek.nutriweek.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiNutrient {

    @JsonProperty("nutrientId")
    private int id;

    @JsonProperty("nutrientName")
    private String name;

    private double value;
    private String unitName;
    private int rank;
}
