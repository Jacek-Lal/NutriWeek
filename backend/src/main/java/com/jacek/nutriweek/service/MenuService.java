package com.jacek.nutriweek.service;

import com.jacek.nutriweek.dto.MenuDTO;
import com.jacek.nutriweek.dto.MenuSummaryDTO;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.Menu;
import com.jacek.nutriweek.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public Menu addMenu(MenuDTO menuDTO) {
        Menu menu = new Menu(menuDTO);

        for(int i=0; i < menuDTO.days(); i++){
            for(int j=0; j < menuDTO.meals(); j++){
                Meal meal = new Meal("Meal " + (j+1), menuDTO.caloriesPerMeal().get(j)/100f, menu);
                menu.getMealList().add(meal);
            }
        }

        return menuRepository.save(menu);
    }

    public List<MenuSummaryDTO> getMenus() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> new MenuSummaryDTO(menu.getId(),
                        menu.getName(),
                        menu.getCalories(),
                        menu.getDays(),
                        menu.getStartDate()))
                .toList();
    }

    public Menu getMenu(long id) {
        return menuRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
