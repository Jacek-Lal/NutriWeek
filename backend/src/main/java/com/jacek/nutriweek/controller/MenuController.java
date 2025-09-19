package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.MenuDTO;
import com.jacek.nutriweek.dto.MenuSummaryDTO;
import com.jacek.nutriweek.model.Menu;
import com.jacek.nutriweek.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> addMenu(@RequestBody MenuDTO menu){
        return ResponseEntity.ok().body(menuService.addMenu(menu));
    }

    @GetMapping
    public ResponseEntity<List<MenuSummaryDTO>> getMenus(){
        return ResponseEntity.ok().body(menuService.getMenus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenu(@PathVariable long id){
        return ResponseEntity.ok().body(menuService.getMenu(id));
    }
}
