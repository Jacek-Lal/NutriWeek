package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int days;
    private int meals;
    private int calories;
    private String startDate;
    private float targetProtein;
    private float targetFat;
    private float targetCarb;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> mealList = new ArrayList<>();
}
