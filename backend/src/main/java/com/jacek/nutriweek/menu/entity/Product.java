package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jacek.nutriweek.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private int fdcId;
    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JsonManagedReference("product-nutrients")
    private List<ProductNutrient> nutrients;

    public Product(String name, int fdcId) {
        this.name = name;
        this.fdcId = fdcId;
        this.nutrients = new ArrayList<>();
    }
}
