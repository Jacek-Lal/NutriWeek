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
public class MealItem extends BaseEntity {

    @Column(nullable = false)
    private float amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meal_id", nullable = false)
    @JsonBackReference("meal-items")
    private Meal meal;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Product product;

    public MealItem(float amount, Meal meal, Product product) {
        this.amount = amount;
        this.meal = meal;
        this.product = product;
    }
}
