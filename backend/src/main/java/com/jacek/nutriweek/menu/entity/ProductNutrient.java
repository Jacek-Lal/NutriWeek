package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jacek.nutriweek.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductNutrient extends BaseEntity {

    @Column(nullable = false)
    private double amount;

    @JsonBackReference("product-nutrients")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Product product;

    @JsonBackReference("nutrient-products")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Nutrient nutrient;

    public ProductNutrient(double amount, Product product, Nutrient nutrient) {
        this.amount = amount;
        this.product = product;
        this.nutrient = nutrient;
    }
}
