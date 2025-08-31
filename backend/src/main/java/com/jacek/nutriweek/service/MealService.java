package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.mapper.MealMapper;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.repository.MealRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public Meal addMeal(MealDTO mealDto) {

        Meal meal = mealMapper.toEntity(mealDto);

        return meal;
//        return mealRepository.save(meal);
    }
}
