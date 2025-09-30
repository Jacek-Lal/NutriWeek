package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.menu.dto.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
                Meal meal = new Meal("Meal " + (j+1),
                        menuRequest.caloriesPerMeal().get(j)/100f,
                        menu.getStartDate().plusDays(i),
                        menu);
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

    public Page<MealsByDate> getMenuMeals(long id, int page, int size) {
        Page<Meal> mealsPage = mealRepository.findByMenuId(id, PageRequest.of(page, size));

        List<MealsByDate> mealsByDate = mealsPage.stream()
                .map(menuMapper::toDto)
                .collect(Collectors.groupingBy(MealDTO::date))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new MealsByDate(e.getKey(), e.getValue()))
                .toList();

        return new PageImpl<>(mealsByDate, mealsPage.getPageable(), mealsPage.getTotalElements());
    }

    public void deleteMenu(long id) {
        menuRepository.deleteById(id);
    }

}
