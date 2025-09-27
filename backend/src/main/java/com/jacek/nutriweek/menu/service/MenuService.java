package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.menu.dto.MealDTO;
import com.jacek.nutriweek.menu.dto.MenuRequest;
import com.jacek.nutriweek.menu.dto.MenuResponse;
import com.jacek.nutriweek.menu.dto.MenuSummary;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Menu;
import com.jacek.nutriweek.menu.mapper.MenuMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.MenuRepository;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final MenuMapper menuMapper;

    public Menu addMenu(String username, MenuRequest menuRequest) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Menu menu = menuMapper.toEntity(menuRequest);
        menu.setOwner(user);

        for(int i=0; i < menu.getDays(); i++){
            for(int j=0; j < menu.getMeals(); j++){
                Meal meal = new Meal("Meal " + (j+1), menuRequest.caloriesPerMeal().get(j)/100f, menu);
                menu.getMealList().add(meal);
            }
        }

        return menuRepository.save(menu);
    }

    public Page<MenuSummary> getMenus(String username, int page, int size) {
        return menuRepository.findAllSummaries(username, PageRequest.of(page, size));
    }

    public MenuResponse getMenu(long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(RuntimeException::new);
        return menuMapper.toDto(menu);
    }

    public Page<MealDTO> getMenuMeals(long id, int page, int size) {
        Page<Meal> mealsPage = mealRepository.findByMenuId(id, PageRequest.of(page, size));
        return mealsPage.map(menuMapper::toDto);
    }

    public void deleteMenu(long id) {
        menuRepository.deleteById(id);
    }

}
