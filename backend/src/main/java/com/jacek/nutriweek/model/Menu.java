package com.jacek.nutriweek.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jacek.nutriweek.dto.MenuDTO;
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

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference
    private List<Meal> mealList;

    public Menu(MenuDTO menuDTO){
        this.name = menuDTO.name();
        this.days = menuDTO.days();
        this.meals = menuDTO.meals();
        this.calories = menuDTO.calories();
        this.startDate = menuDTO.startDate();
        this.targetProtein = menuDTO.targetProtein();
        this.targetFat = menuDTO.targetFat();
        this.targetCarb = menuDTO.targetCarb();
        this.mealList = new ArrayList<>();
    }
}
