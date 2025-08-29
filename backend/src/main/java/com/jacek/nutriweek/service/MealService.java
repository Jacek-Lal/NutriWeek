package com.jacek.nutriweek.service;

import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.repository.MealRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;

    public List<Meal> addMeal(List<Meal> mealList) {


        return mealRepository.saveAll(mealList);
    }
}
