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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final MenuMapper menuMapper;

    public Menu addMenu(String username, MenuRequest menuRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with given username doesn't exist"));

        Menu menu = menuMapper.toEntity(menuRequest);

        menu.setOwner(user);

        for(int i=0; i < menu.getDays(); i++){
            for(int j=0; j < menu.getMeals(); j++){
                int targetKcal = (int) ((menuRequest.caloriesPerMeal().get(j) / 100f) * menuRequest.calories());
                Meal meal = new Meal("Meal " + (j+1),
                        targetKcal,
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
        Page<LocalDate> datesPage = mealRepository.findDistinctDatesByMenuId(id, PageRequest.of(page, size));

        List<MealsByDate> content = datesPage.getContent().stream()
                .map(date -> {
                    List<MealDTO> meals = mealRepository.findByMenuIdAndDate(id, date).stream()
                            .map(menuMapper::toDto)
                            .toList();
                    return new MealsByDate(date, meals);
                })
                .toList();

        return new PageImpl<>(content, datesPage.getPageable(), datesPage.getTotalElements());
    }

    public MealDTO addMenuMeal(long id, MealRequest mealReq) {
        Menu menu = menuRepository.findById(id).orElseThrow(RuntimeException::new);
        Meal meal = new Meal(mealReq.name(),
                mealReq.targetKcal(),
                mealReq.date(),
                menu);

        mealRepository.save(meal);

        return menuMapper.toDto(meal);
    }

    public void deleteMenu(long id) {
        menuRepository.deleteById(id);
    }


}
