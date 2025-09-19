package com.jacek.nutriweek.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MealItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float amount;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonBackReference("meal-items")
    private Meal meal;

    @ManyToOne(optional = false)
    private Product product;
}
