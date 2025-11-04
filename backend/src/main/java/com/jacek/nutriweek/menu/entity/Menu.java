package com.jacek.nutriweek.menu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jacek.nutriweek.common.BaseEntity;
import com.jacek.nutriweek.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int days;

    @Column(nullable = false)
    private int meals;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private float targetProtein;

    @Column(nullable = false)
    private float targetFat;

    @Column(nullable = false)
    private float targetCarb;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(
            mappedBy = "menu",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Meal> mealList = new ArrayList<>();
}
