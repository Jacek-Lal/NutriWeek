package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    @Query("""
    select distinct m.date
    from Meal m
    where m.menu.id = :menuId
    order by m.date
""")
    Page<LocalDate> findDistinctDatesByMenuId(@Param("menuId") long menuId, PageRequest of);

    List<Meal> findByMenuIdAndDate(long menuId, LocalDate date);
}
