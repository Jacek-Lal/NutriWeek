package com.jacek.nutriweek.repository;

import com.jacek.nutriweek.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Integer> {
}
