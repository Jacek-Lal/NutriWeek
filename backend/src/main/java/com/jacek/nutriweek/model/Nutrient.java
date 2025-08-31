package com.jacek.nutriweek.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String unit;

    @OneToMany(mappedBy = "nutrient", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProductNutrient> products;

    public Nutrient(String name, String unit){
        this.name = name;
        this.unit = unit;
    }
}
