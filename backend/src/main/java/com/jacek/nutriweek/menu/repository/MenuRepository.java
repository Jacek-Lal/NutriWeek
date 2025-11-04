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
      SELECT m 
      FROM Menu m
      JOIN m.owner o
      WHERE o.username = :username AND m.id = :id 
      """)
    Optional<Menu> findByOwnerAndId(@Param("username") String username, @Param("id") long id);

    @Query("""
            SELECT m.id, m.name, m.days, m.startDate, m.createdAt
            FROM Menu m
            JOIN m.owner o
            WHERE o.username = :username
            ORDER BY m.createdAt DESC
            """)
    Page<MenuSummary> findAllSummaries(@Param("username") String username, Pageable pageable);
}
