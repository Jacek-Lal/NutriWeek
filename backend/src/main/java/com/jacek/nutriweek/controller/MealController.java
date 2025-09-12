package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.dto.ProductDTO;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealController {
    private final MealService mealService;

    @PostMapping
    public ResponseEntity<Meal> addMeal(@RequestBody MealDTO mealDto){
        return ResponseEntity.ok().body(mealService.addMeal(mealDto));
    }
    @GetMapping("/recent")
    public ResponseEntity<List<ProductDTO>> getRecentProducts(@RequestParam int limit){
        return ResponseEntity.ok().body(mealService.getRecentProducts(limit));
    }

}
