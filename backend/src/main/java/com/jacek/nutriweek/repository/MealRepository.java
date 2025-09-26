package com.jacek.nutriweek.repository;

import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query(value = """
            SELECT DISTINCT ON (p.id) p.*
            FROM meal_item mi
            JOIN product p ON mi.product_id = p.id
            ORDER BY p.id, mi.id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Product> findRecentProducts(@Param("limit") int limit);

    Page<Meal> findByMenuId(long id, PageRequest of);
}
