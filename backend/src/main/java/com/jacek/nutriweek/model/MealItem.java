package com.jacek.nutriweek.model;

import com.jacek.nutriweek.dto.ApiProduct;
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

    @OneToOne(optional = false)
    private Product product;
}
