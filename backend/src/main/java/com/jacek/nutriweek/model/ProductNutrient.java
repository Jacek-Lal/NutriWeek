package com.jacek.nutriweek.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(optional = false)
    @JsonBackReference("product-nutrients")
    private Product product;

    @ManyToOne(optional = false)
    @JsonBackReference("nutrient-products")
    private Nutrient nutrient;

    public ProductNutrient(double amount, Product product, Nutrient nutrient) {
        this.amount = amount;
        this.product = product;
        this.nutrient = nutrient;
    }
}
