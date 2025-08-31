package com.jacek.nutriweek.model;

import com.jacek.nutriweek.dto.MealDTO;
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
    private float targetKcal;
    private float targetCarb;
    private float targetFat;
    private float targetProtein;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meal_id")
    private List<MealItem> mealItems;
}
