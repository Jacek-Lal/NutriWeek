package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping("/meals")
    public ResponseEntity<Meal> addMeal(@RequestBody MealDTO mealDto){
        return ResponseEntity.ok().body(mealService.addMeal(mealDto));
    }

}
