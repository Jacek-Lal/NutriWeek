package com.jacek.nutriweek.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity

public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String unit;

    @OneToMany(mappedBy = "nutrient")
    private List<ProductNutrient> products;

    public Nutrient(String name, String unit){
        this.name = name;
        this.unit = unit;
    }

    public String getKey(){
        return this.name + "|" + this.unit;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Nutrient nutrient)) return false;
        return Objects.equals(name, nutrient.name) && Objects.equals(unit, nutrient.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit);
    }
}
