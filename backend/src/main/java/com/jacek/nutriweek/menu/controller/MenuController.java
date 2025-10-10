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
        String username = auth.getName();
        return ResponseEntity.ok().body(menuService.addMenu(username, menu));
    }

    @GetMapping
    public ResponseEntity<Page<MenuSummary>> getMenus(@RequestParam int page,
                                                      @RequestParam int size,
                                                      Authentication auth){
        String username = auth.getName();
        return ResponseEntity.ok().body(menuService.getMenus(username, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable long id){
        return ResponseEntity.ok().body(menuService.getMenu(id));
    }

    @GetMapping("/{id}/meals")
    public ResponseEntity<Page<MealsByDate>> getMenuMeals(@PathVariable long id,
                                                          @RequestParam int page,
                                                          @RequestParam int size){
        return ResponseEntity.ok().body(menuService.getMenuMeals(id, page, size));
    }
    @PostMapping("/{id}/meals")
    public ResponseEntity<MealDTO> addMenuMeal(@PathVariable long id,
                                               @Valid @RequestBody MealRequest meal){
        return ResponseEntity.ok().body(menuService.addMenuMeal(id, meal));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id){
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
