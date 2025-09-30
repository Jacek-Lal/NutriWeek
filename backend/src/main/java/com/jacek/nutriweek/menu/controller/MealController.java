package com.jacek.nutriweek.menu.controller;

import com.jacek.nutriweek.menu.dto.MealDTO;
import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.service.MealService;
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
    @PutMapping("/{mealId}/items")
    public ResponseEntity<Meal> updateMealItems(@PathVariable Long mealId,
                                                @RequestBody List<MealItemDTO> items){

        return ResponseEntity.ok().body(mealService.updateMealItems(mealId, items));
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId){
        mealService.deleteMeal(mealId);
        return ResponseEntity.noContent().build();
    }
}
