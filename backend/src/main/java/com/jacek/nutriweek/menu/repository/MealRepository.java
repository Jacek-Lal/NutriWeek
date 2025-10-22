package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
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
    List<Product> findRecentProductsByUsername(@Param("username") String username, Pageable pageable);


    @Query("""
    select distinct m.date
    from Meal m
    where m.menu.id = :menuId
    order by m.date
""")
    Page<LocalDate> findDistinctDatesByMenuId(@Param("menuId") long menuId, PageRequest of);

    List<Meal> findByMenuIdAndDate(long menuId, LocalDate date);
}
