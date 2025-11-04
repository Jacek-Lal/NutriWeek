package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.exception.MenuNotFoundException;
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
import java.util.*;

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
        Menu menu = menuRepository.findById(id).orElseThrow(() ->
                new MenuNotFoundException("Menu with id " + id + " does not exist"));
        return menuMapper.toDto(menu);
    }

    public Page<MealsByDate> getMenuMeals(long menuId, int page, int size) {
        Page<LocalDate> datesPage =
                mealRepository.findDistinctDatesByMenuId(menuId, PageRequest.of(page, size));

        List<LocalDate> dates = datesPage.getContent();
        if (dates.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), datesPage.getPageable(), 0);
        }

        List<MealFlatDTO> flatData = mealRepository.findMealData(menuId, dates);

        Map<LocalDate, Map<Long, MealDTO>> mealsByDate = new TreeMap<>();

        for (MealFlatDTO row : flatData) {
            Map<Long, MealDTO> mealsOnDate =
                    mealsByDate.computeIfAbsent(row.mealDate(), d -> new LinkedHashMap<>());

            MealDTO meal = mealsOnDate.get(row.mealId());
            List<MealItemDTO> mealItems;
            if (meal == null) {
                mealItems = new ArrayList<>();
                meal = new MealDTO(row.mealId(), row.mealName(), row.targetKcal(),
                        row.mealDate(), mealItems);
                mealsOnDate.put(row.mealId(), meal);

                if (!row.containProducts())
                    continue;
            } else {
                mealItems = new ArrayList<>(meal.mealItems());
            }

            MealItemDTO existingItem = mealItems.stream()
                    .filter(mi -> mi.product().fdcId() == row.fdcId())
                    .findFirst()
                    .orElse(null);

            ProductDTO product;
            List<NutrientDTO> nutrients;

            if (existingItem == null) {
                nutrients = new ArrayList<>();
                product = new ProductDTO(row.fdcId(), row.productName(), nutrients);
                mealItems.add(new MealItemDTO(product, row.productAmount()));
            } else {
                product = existingItem.product();
                nutrients = new ArrayList<>(product.nutrients());
            }

            if (row.containNutrients()){
                NutrientDTO nutrient = new NutrientDTO(
                        row.nutrientName(),
                        row.nutrientUnit(),
                        row.nutrientAmount()
                );
                nutrients.add(nutrient);
            }

            product = new ProductDTO(product.fdcId(), product.name(), nutrients);

            ProductDTO finalProduct = product;
            mealItems.removeIf(mi -> mi.product().fdcId() == finalProduct.fdcId());
            mealItems.add(new MealItemDTO(product, row.productAmount()));

            mealsOnDate.put(row.mealId(),
                    new MealDTO(row.mealId(), row.mealName(), row.targetKcal(),
                            row.mealDate(), mealItems));
        }

        List<MealsByDate> content = mealsByDate.entrySet().stream()
                .map(entry -> new MealsByDate(entry.getKey(),
                        new ArrayList<>(entry.getValue().values())))
                .toList();

        return new PageImpl<>(content, datesPage.getPageable(), datesPage.getTotalElements());
    }


    public MealDTO addMenuMeal(long id, MealRequest mealReq) {
        Menu menu = menuRepository.findById(id).orElseThrow(()->
                new MenuNotFoundException("Menu with id " + id + " does not exist"));

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
