package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByFdcId(int fdcId);
    List<Product> findAllByFdcIdIn(Collection<Integer> fdcIds);
}
