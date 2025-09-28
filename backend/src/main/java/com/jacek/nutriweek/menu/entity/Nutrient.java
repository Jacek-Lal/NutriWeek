package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jacek.nutriweek.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Nutrient extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String unit;

    @OneToMany(
            mappedBy = "nutrient",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    @JsonManagedReference("nutrient-products")
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
