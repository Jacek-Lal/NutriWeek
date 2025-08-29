package com.jacek.nutriweek.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Meal {
    @Id
    @Column(unique = true)
    private Long fdcId;
    private String description;

}
