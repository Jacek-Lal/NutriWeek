package com.jacek.nutriweek.integration.menu;

import com.jacek.nutriweek.menu.entity.Nutrient;
import com.jacek.nutriweek.menu.entity.Product;
import com.jacek.nutriweek.menu.entity.ProductNutrient;
import com.jacek.nutriweek.menu.repository.NutrientRepository;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class NutrientRepositoryTest {

    @Autowired NutrientRepository nutrientRepository;
    @Autowired ProductRepository productRepository;
    @Autowired EntityManager em;

    @Test
    void shouldCascadeDeleteProductNutrients_whenDeletingNutrient() {
        Nutrient carb = nutrientRepository.save(new Nutrient("Carbohydrates", "G"));
        em.flush();

        Product product = new Product("Rice", 321);

        ProductNutrient pn = new ProductNutrient(70.0, product, carb);

        product.getNutrients().add(pn);
        carb.getProducts().add(pn);

        productRepository.save(product);
        em.flush();
        em.clear();

        nutrientRepository.delete(carb);
        em.flush();

        Long pnCount = em.createQuery("select count(pn) from ProductNutrient pn", Long.class)
                .getSingleResult();
        assertEquals(0L, pnCount);
        assertEquals(0L, nutrientRepository.count());
        assertEquals(1L, productRepository.count());
    }

}
