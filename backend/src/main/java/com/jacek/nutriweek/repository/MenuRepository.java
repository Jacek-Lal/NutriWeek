package com.jacek.nutriweek.repository;

import com.jacek.nutriweek.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
