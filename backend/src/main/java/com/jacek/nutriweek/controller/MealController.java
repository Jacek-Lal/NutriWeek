package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.menu.MealDTO;
import com.jacek.nutriweek.dto.menu.MealItemDTO;
import com.jacek.nutriweek.dto.menu.ProductDTO;
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
    @PutMapping("/{mealId}/items")
    public ResponseEntity<Meal> updateMealItems(@PathVariable Long mealId,
                                                @RequestBody List<MealItemDTO> items){

        return ResponseEntity.ok().body(mealService.updateMealItems(mealId, items));
    }

}
