package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.dto.MealFlatDTO;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT m FROM Meal m WHERE m.menu.owner.username = :username AND m.id = :id")
    Optional<Meal> findByOwnerAndId(@Param("username") String username,
                                    @Param("id") long id);

    @Query("""
        SELECT p
        FROM MealItem mi
        JOIN mi.product p
        JOIN mi.meal m
        JOIN m.menu mn
        JOIN mn.owner u
        WHERE u.username = :username
        GROUP BY p
        ORDER BY MAX(mi.id) DESC
    """)
    List<Product> findRecentProductsByUsername(@Param("username") String username,
                                               Pageable pageable);


    @Query("""
        SELECT DISTINCT m.date
        FROM Meal m
        WHERE m.menu.id = :menuId
        AND m.menu.owner.username = :username
        ORDER BY m.date
    """)
    Page<LocalDate> findDistinctDatesByMenuId(@Param("username") String username,
                                              @Param("menuId") long menuId,
                                              Pageable pageable);

    List<Meal> findByMenuIdAndDate(long menuId, LocalDate date);

    @Query("""
        SELECT new com.jacek.nutriweek.menu.dto.MealFlatDTO(
            m.id, m.date, m.name, m.targetKcal,
            mi.amount,
            p.name, p.fdcId,
            n.name, n.unit,
            pn.amount
        )
        FROM Meal m
        LEFT JOIN m.mealItems mi
        LEFT JOIN mi.product p
        LEFT JOIN p.nutrients pn
        LEFT JOIN pn.nutrient n
        WHERE m.menu.owner.username = :username AND m.menu.id = :menuId AND m.date IN :dates
        ORDER BY m.date ASC, m.id ASC
    """)
    List<MealFlatDTO> findMealData(@Param("username") String username,
                                   @Param("menuId") long menuId,
                                   @Param("dates") Collection<LocalDate> dates);
}
