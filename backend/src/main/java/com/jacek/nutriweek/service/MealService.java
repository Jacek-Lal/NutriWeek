package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.dto.ProductDTO;
import com.jacek.nutriweek.mapper.MealMapper;
import com.jacek.nutriweek.mapper.ProductMapper;
import com.jacek.nutriweek.model.*;
import com.jacek.nutriweek.repository.MealRepository;
import com.jacek.nutriweek.repository.NutrientRepository;
import com.jacek.nutriweek.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Meal addMeal(MealDTO mealDto) {
        Meal meal = mealMapper.toEntity(mealDto);

        Set<Integer> fdcIds = meal.getMealItems().stream()
                .map(mi -> mi.getProduct().getFdcId())
                .collect(Collectors.toSet());

        Map<Integer, Product> existingProducts = productRepository.findAllByFdcIdIn(fdcIds)
                .stream()
                .collect(Collectors.toMap(Product::getFdcId, p -> p));

        Map<String, Nutrient> existingNutrients = nutrientRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nutrient::getKey, n -> n));


        for (MealItem mealItem : meal.getMealItems()){
            Product product = existingProducts.get(mealItem.getProduct().getFdcId());

            if(product == null)
                product = productRepository.save(mealItem.getProduct());

            for (ProductNutrient pn : product.getNutrients()){
                Nutrient nutrient = existingNutrients.get(pn.getNutrient().getKey());
                if(nutrient == null){
                    nutrient = nutrientRepository.save(pn.getNutrient());
                    existingNutrients.put(nutrient.getKey(), nutrient);
                }
                pn.setNutrient(nutrient);
                pn.setProduct(product);
            }

            mealItem.setProduct(product);
        }
        return mealRepository.save(meal);
    }

    public List<ProductDTO> getRecentProducts(int limit) {
        return productMapper.toDtoList(mealRepository.findRecentProducts(limit));
    }
}
