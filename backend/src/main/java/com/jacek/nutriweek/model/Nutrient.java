package com.jacek.nutriweek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Nutrient {
    private String number;
    private String name;
    private double amount;
    private String unitName;
}
