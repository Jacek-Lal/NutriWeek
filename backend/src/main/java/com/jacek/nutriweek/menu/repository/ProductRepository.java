package com.jacek.nutriweek.menu.repository;

import com.jacek.nutriweek.menu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByFdcId(int fdcId);
    List<Product> findAllByFdcIdIn(Collection<Integer> fdcIds);

    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN FETCH p.nutrients pn
    LEFT JOIN FETCH pn.nutrient n
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<Product> findByQuery(@Param("query") String query, Pageable pageable);
}
