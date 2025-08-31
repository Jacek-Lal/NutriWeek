package com.jacek.nutriweek.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiProduct {
    private int fdcId;

    @JsonAlias("description")
    @JsonProperty("name")
    private String name;

    @JsonAlias("foodNutrients")
    @JsonProperty("nutrients")
    private List<ApiNutrient> nutrients;

}
