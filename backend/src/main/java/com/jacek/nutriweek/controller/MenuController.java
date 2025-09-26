package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.menu.MealDTO;
import com.jacek.nutriweek.dto.menu.MenuRequest;
import com.jacek.nutriweek.dto.menu.MenuResponse;
import com.jacek.nutriweek.dto.menu.MenuSummary;
import com.jacek.nutriweek.model.Menu;
import com.jacek.nutriweek.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> addMenu(@RequestBody MenuRequest menu){
        return ResponseEntity.ok().body(menuService.addMenu(menu));
    }

    @GetMapping
    public ResponseEntity<Page<MenuSummary>> getMenus(@RequestParam int page,
                                                      @RequestParam int size){
        return ResponseEntity.ok().body(menuService.getMenus(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable long id){
        return ResponseEntity.ok().body(menuService.getMenu(id));
    }

    @GetMapping("/{id}/meals")
    public ResponseEntity<Page<MealDTO>> getMenuMeals(@PathVariable long id,
                                                      @RequestParam int page,
                                                      @RequestParam int size){
        return ResponseEntity.ok().body(menuService.getMenuMeals(id, page, size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id){
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
