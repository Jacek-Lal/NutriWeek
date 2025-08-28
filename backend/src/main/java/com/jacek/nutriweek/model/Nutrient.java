package com.jacek.nutriweek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Nutrient {

    @JsonProperty("nutrientId")
    private int id;

    @JsonProperty("nutrientName")
    private String name;

    private double value;
    private String unitName;
    private int rank;
}
