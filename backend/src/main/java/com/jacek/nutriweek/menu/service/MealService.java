package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.exception.MealNotFoundException;
import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.NutrientDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.entity.*;
import com.jacek.nutriweek.menu.mapper.MealMapper;
import com.jacek.nutriweek.menu.mapper.ProductMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.NutrientRepository;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;

    private final MealMapper mealMapper;
    private final ProductMapper productMapper;

    public void updateMealItems(String username, Long mealId, List<MealItemDTO> items) {
        Meal meal =  mealRepository.findByOwnerAndId(username, mealId).orElseThrow(()->
                new MealNotFoundException("Meal with id " + mealId + " not found"));
        meal.getMealItems().clear();

        if(items.isEmpty()) {
            mealRepository.save(meal);
            return;
        }

        Set<Integer> fdcIds = items.stream()
                .map(mi -> mi.product().fdcId())
                .collect(Collectors.toSet());

        Map<Integer, Product> existingProducts = productRepository.findAllByFdcIdIn(fdcIds)
                .stream()
                .collect(Collectors.toMap(Product::getFdcId, p -> p));

        Map<String, Nutrient> existingNutrients = nutrientRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nutrient::getKey, n -> n));

        for (MealItemDTO reqItem : items){
            ProductDTO reqProduct = reqItem.product();
            Product product = existingProducts.get(reqProduct.fdcId());

            if(product == null){
                product = createProduct(reqProduct, existingNutrients);
                existingProducts.put(product.getFdcId(), product);
            }
            MealItem mi = new MealItem(reqItem.amount(), meal, product);
            meal.getMealItems().add(mi);
        }

        mealRepository.save(meal);
    }

    protected Product createProduct(ProductDTO reqProduct, Map<String, Nutrient> existingNutrients){
        Product newProduct = new Product(reqProduct.name(), reqProduct.fdcId());

        Set<ProductNutrient> pns = new HashSet<>();
        for(NutrientDTO reqNut : reqProduct.nutrients()){
            Nutrient newNut = new Nutrient(reqNut.name(), reqNut.unit());
            String key = newNut.getKey();

            Nutrient managedNut = existingNutrients.get(key);
            if(managedNut == null){
                managedNut = nutrientRepository.save(newNut);
                existingNutrients.put(key, managedNut);
            }
            pns.add(new ProductNutrient(reqNut.value(), newProduct, managedNut));
        }
        newProduct.setNutrients(pns);
        return productRepository.save(newProduct);
    }

    public List<ProductDTO> getRecentProducts(String username, int limit) {
        return productMapper.toDtoList(mealRepository.findRecentProductsByUsername(username, PageRequest.of(0, limit)));
    }

    public void deleteMeal(String username, Long mealId) {
        Meal meal =  mealRepository.findByOwnerAndId(username, mealId).orElseThrow(()->
                new MealNotFoundException("Meal with id " + mealId + " not found"));

        mealRepository.delete(meal);
    }
}
