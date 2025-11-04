package com.jacek.nutriweek.menu.controller;

import com.jacek.nutriweek.menu.dto.*;
import com.jacek.nutriweek.menu.entity.Menu;
import com.jacek.nutriweek.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> addMenu(@Valid @RequestBody MenuRequest menu, Authentication auth){
        return ResponseEntity.ok().body(menuService.addMenu(auth.getName(), menu));
    }

    @GetMapping
    public ResponseEntity<Page<MenuSummary>> getMenus(@RequestParam int page,
                                                      @RequestParam int size,
                                                      Authentication auth){

        return ResponseEntity.ok().body(menuService.getMenus(auth.getName(), page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable long id,
                                                Authentication auth){

        return ResponseEntity.ok().body(menuService.getMenu(auth.getName(), id));
    }

    @GetMapping("/{id}/meals")
    public ResponseEntity<Page<MealsByDate>> getMenuMeals(@PathVariable long id,
                                                          @RequestParam int page,
                                                          @RequestParam int size,
                                                          Authentication auth){
        return ResponseEntity.ok().body(menuService.getMenuMeals(auth.getName(), id, page, size));
    }
    @PostMapping("/{id}/meals")
    public ResponseEntity<MealDTO> addMenuMeal(@PathVariable long id,
                                               @Valid @RequestBody MealRequest meal,
                                               Authentication auth){
        return ResponseEntity.ok().body(menuService.addMenuMeal(auth.getName(), id, meal));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id,
                                           Authentication auth){
        menuService.deleteMenu(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
