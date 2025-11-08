package com.jacek.nutriweek.integration.menu;

import com.jacek.nutriweek.menu.repository.NutrientRepository;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.jacek.nutriweek.menu.entity.Product;
import com.jacek.nutriweek.menu.entity.ProductNutrient;
import com.jacek.nutriweek.menu.entity.Nutrient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ProductRepositoryTest {

    @Autowired ProductRepository productRepository;
    @Autowired NutrientRepository nutrientRepository;
    @Autowired EntityManager em;

    @Test
    void shouldCascadePersistProductNutrients_whenSavingProduct() {
        Nutrient protein = nutrientRepository.save(new Nutrient("Protein", "G"));
        Nutrient fat = nutrientRepository.save(new Nutrient("Fat", "G"));

        Product product = new Product("Chicken", 123);
        product.setNutrients(Set.of(
                new ProductNutrient(20.0, product, protein),
                new ProductNutrient(5.0, product, fat)
        ));

        Product saved = productRepository.save(product);
        em.flush();
        em.clear();

        Product fetched = productRepository.findById(saved.getId()).orElseThrow();
        Long pnCount = em.createQuery("SELECT COUNT(pn) FROM ProductNutrient pn", Long.class)
                .getSingleResult();
        assertEquals(2L, pnCount);
        assertEquals("Chicken", fetched.getName());
        assertEquals(2, nutrientRepository.count());
        assertTrue(fetched.getNutrients()
                .stream()
                .anyMatch(n -> n.getNutrient().getName().equals("Protein")));
    }

    @Test
    void shouldCascadeRemoveProductNutrients_whenDeletingProduct() {
        Nutrient carb = nutrientRepository.save(new Nutrient("Carbohydrates", "G"));
        Product product = new Product("Rice", 321);

        product.setNutrients(Set.of(new ProductNutrient(70.0, product, carb)));
        Product saved = productRepository.save(product);
        em.flush();

        productRepository.delete(saved);
        em.flush();

        assertEquals(0, productRepository.count());
        Long nutrientLinks = em
                .createQuery("SELECT COUNT(pn) FROM ProductNutrient pn", Long.class)
                .getSingleResult();
        assertEquals(0, nutrientLinks);

    }

    @Test
    void shouldFindProductByFdcId() {
        Product product = new Product("Eggs", 555);
        productRepository.save(product);

        Optional<Product> found = productRepository.findByFdcId(555);

        assertTrue(found.isPresent());
        assertEquals("Eggs", found.get().getName());
    }

    @Test
    void shouldFindAllByFdcIdIn() {
        Product p1 = new Product("Broccoli", 111);
        Product p2 = new Product("Potatoes", 222);
        Product p3 = new Product("Chicken", 333);
        productRepository.saveAll(List.of(p1, p2, p3));

        List<Product> result = productRepository.findAllByFdcIdIn(List.of(111, 333));
        List<String> productNames = result.stream().map(Product::getName).toList();

        assertEquals(2, result.size());
        assertTrue(productNames.containsAll(List.of("Broccoli", "Chicken")));
    }
}
