package com.jacek.nutriweek.service;

import com.jacek.nutriweek.model.Menu;
import com.jacek.nutriweek.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> getMenus() {
        return menuRepository.findAll();
    }

    public Menu getMenu(long id) {
        return menuRepository.findById(id).orElse(null);
    }
}
