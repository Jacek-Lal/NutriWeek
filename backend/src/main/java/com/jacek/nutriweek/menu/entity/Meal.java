package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float caloriesPercent;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @JsonManagedReference("meal-items")
    @OneToMany(mappedBy = "meal", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> mealItems;

    public Meal(String name, float caloriesPercent, Menu menu){
        this.name = name;
        this.caloriesPercent = caloriesPercent;
        this.menu = menu;
    }
}
