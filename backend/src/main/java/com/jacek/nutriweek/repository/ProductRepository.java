package com.jacek.nutriweek.repository;

import com.jacek.nutriweek.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByFdcId(int fdcId);
    List<Product> findAllByFdcIdIn(Collection<Integer> fdcIds);
}
