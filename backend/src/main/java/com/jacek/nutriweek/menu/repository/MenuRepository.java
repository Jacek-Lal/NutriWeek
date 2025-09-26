package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.dto.MenuSummary;
import com.jacek.nutriweek.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("""
      SELECT DISTINCT m FROM Menu m
      LEFT JOIN FETCH m.mealList ml
      LEFT JOIN FETCH ml.mealItems mi
      LEFT JOIN FETCH mi.product p
      LEFT JOIN FETCH p.nutrients pn
      LEFT JOIN FETCH pn.nutrient n
      WHERE m.id = :id
    """)
    Optional<Menu> findMenuWithAll(@Param("id") long id);

    @Query("""
            SELECT m.id, m.name, m.calories, m.days, m.startDate
            FROM Menu m
            """)
    Page<MenuSummary> findAllSummaries(Pageable pageable);

}
