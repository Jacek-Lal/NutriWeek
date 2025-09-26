package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
    Optional<Nutrient> findByNameAndUnit(String name, String unit);
}
