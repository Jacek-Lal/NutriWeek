package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.mapper.MealMapper;
import com.jacek.nutriweek.model.*;
import com.jacek.nutriweek.repository.MealRepository;
import com.jacek.nutriweek.repository.NutrientRepository;
import com.jacek.nutriweek.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;
    private final MealMapper mealMapper;

    public Meal addMeal(MealDTO mealDto) {

        Meal meal = mealMapper.toEntity(mealDto);

        for (MealItem mealItem : meal.getMealItems()){
            Product product = getProduct(mealItem);
            mealItem.setProduct(product);
        }
        return mealRepository.save(meal);
    }

    private Product getProduct(MealItem mealItem){
        for (ProductNutrient pn : mealItem.getProduct().getNutrients()){
            Nutrient nutrient = getNutrient(pn);
            pn.setNutrient(nutrient);
        }
        return productRepository.findByFdcId(mealItem.getProduct().getFdcId())
                .orElseGet(() -> productRepository.save(mealItem.getProduct()));
    }

    private Nutrient getNutrient(ProductNutrient pn){
        return nutrientRepository
                .findByNameAndUnit(pn.getNutrient().getName(), pn.getNutrient().getUnit())
                .orElseGet(()-> nutrientRepository.save(pn.getNutrient()));

    }
}
