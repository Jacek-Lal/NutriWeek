package com.jacek.nutriweek.controller;

import com.jacek.nutriweek.dto.MenuRequestDTO;
import com.jacek.nutriweek.dto.MenuResponseDTO;
import com.jacek.nutriweek.dto.MenuSummaryDTO;
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
    public ResponseEntity<Menu> addMenu(@RequestBody MenuRequestDTO menu){
        return ResponseEntity.ok().body(menuService.addMenu(menu));
    }

    @GetMapping
    public ResponseEntity<Page<MenuSummaryDTO>> getMenus(@RequestParam int page,
                                                         @RequestParam int size){
        return ResponseEntity.ok().body(menuService.getMenus(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenu(@PathVariable long id){
        return ResponseEntity.ok().body(menuService.getMenu(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id){
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
