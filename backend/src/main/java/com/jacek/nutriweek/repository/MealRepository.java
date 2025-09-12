package com.jacek.nutriweek.repository;

import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {
    @Query("""
            SELECT p FROM MealItem mi
            JOIN mi.product p
            ORDER BY mi.id DESC
            LIMIT :limit
            """)
    List<Product> findRecentProducts(@Param("limit") int limit);
}
