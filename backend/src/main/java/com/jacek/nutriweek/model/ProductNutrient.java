package com.jacek.nutriweek.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductNutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Nutrient nutrient;

    public ProductNutrient(double amount, Product product, Nutrient nutrient) {
        this.amount = amount;
        this.product = product;
        this.nutrient = nutrient;
    }

}
