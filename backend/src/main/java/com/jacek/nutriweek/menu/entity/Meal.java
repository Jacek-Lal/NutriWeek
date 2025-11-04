package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jacek.nutriweek.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Meal extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int targetKcal;

    private LocalDate date;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @JsonManagedReference("meal-items")
    @OneToMany(
            mappedBy = "meal",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<MealItem> mealItems = new ArrayList<>();

    public Meal(String name, int targetKcal, LocalDate date, Menu menu){
        this.name = name;
        this.targetKcal = targetKcal;
        this.date = date;
        this.menu = menu;
    }
}
