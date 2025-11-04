package com.jacek.nutriweek.menu.controller;

import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealController {
    private final MealService mealService;

    @GetMapping("/recent")
    public ResponseEntity<List<ProductDTO>> getRecentProducts(@RequestParam int limit,
                                                              Authentication auth){
        return ResponseEntity.ok().body(mealService.getRecentProducts(auth.getName(), limit));
    }
    @PutMapping("/{mealId}/items")
    public ResponseEntity<Meal> updateMealItems(@PathVariable Long mealId,
                                                @RequestBody List<MealItemDTO> items,
                                                Authentication auth){

        mealService.updateMealItems(auth.getName(), mealId, items);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId,
                                           Authentication auth){
        mealService.deleteMeal(auth.getName(), mealId);
        return ResponseEntity.noContent().build();
    }
}
