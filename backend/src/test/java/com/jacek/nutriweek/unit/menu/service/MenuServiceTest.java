package com.jacek.nutriweek.unit.menu.service;

import com.jacek.nutriweek.common.exception.ResourceNotFoundException;
import com.jacek.nutriweek.menu.dto.*;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Menu;
import com.jacek.nutriweek.menu.mapper.MenuMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.MenuRepository;
import com.jacek.nutriweek.menu.service.MenuService;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock MenuRepository menuRepository;
    @Mock MealRepository mealRepository;
    @Mock UserRepository userRepository;
    @Mock MenuMapper menuMapper;

    @InjectMocks
    MenuService menuService;

    private final String USERNAME = "john";

    @Test
    void shouldSaveMenu_whenValidRequest(){
        User user = new User("john", "encoded", "john@example.com", true);
        MenuRequest req = new MenuRequest("Menu", 7, 3, 2000, "2025-09-30",
                30, 30, 40, Arrays.asList(20, 50, 30));
        Menu mappedMenu = new Menu();
        mappedMenu.setDays(req.days());
        mappedMenu.setName(req.name());
        mappedMenu.setMeals(req.meals());
        mappedMenu.setStartDate(LocalDate.parse(req.startDate()));
        mappedMenu.setTargetCarb(req.targetCarb());
        mappedMenu.setTargetFat(req.targetFat());
        mappedMenu.setTargetProtein(req.targetProtein());

        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));
        when(menuMapper.toEntity(req)).thenReturn(mappedMenu);

        menuService.addMenu(USERNAME, req);

        ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository).save(captor.capture());
        Menu savedMenu = captor.getValue();

        int expectedMeals = req.days() * req.meals();
        int expectedMealKcal = (int)(req.calories() * req.caloriesPerMeal().get(1)/100f);
        LocalDate expectedMealDate = LocalDate.parse(req.startDate()).plusDays(3);

        assertEquals(expectedMeals, savedMenu.getMealList().size());
        assertEquals(expectedMealKcal, savedMenu.getMealList().get(1).getTargetKcal());
        assertEquals(expectedMealDate, savedMenu.getMealList().get(10).getDate());
        assertSame(user, savedMenu.getOwner());
    }

    @Test
    void shouldThrow_whenUserNotFound(){
        MenuRequest req = new MenuRequest("Menu", 7, 3, 2000, "2025-09-30",
                30, 30, 40, Arrays.asList(20, 50, 30));

        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuService.addMenu(USERNAME, req));

        verify(menuMapper, never()).toEntity(req);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void shouldReturnMenuResponse_whenValidId(){
        long id = 0;
        Menu menu = new Menu();

        when(menuRepository.findByOwnerAndId(eq(USERNAME), eq(id))).thenReturn(Optional.of(menu));

        menuService.getMenu(USERNAME, id);

        verify(menuMapper).toDto(menu);
    }

    @Test
    void shouldThrow_whenInvalidIdOrUsername(){
        when(menuRepository.findByOwnerAndId(anyString(), anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuService.getMenu(USERNAME, 0));

        verify(menuMapper, never()).toDto(any(Menu.class));
    }

    @Test
    void shouldReturnMealsByDate_whenRepositoryReturnsMultipleDates() {
        long menuId = 1L;
        int page = 0, size = 2;

        LocalDate date1 = LocalDate.of(2025, 10, 1);
        LocalDate date2 = LocalDate.of(2025, 10, 2);
        Page<LocalDate> datesPage = new PageImpl<>(List.of(date1, date2), PageRequest.of(page, size), 2);

        List<MealFlatDTO> flatData = List.of(
                new MealFlatDTO(1L, date1, "Meal 1", 500, 100.0, "Chicken", 123,
                        "Protein", "g", 25.0),
                new MealFlatDTO(1L, date1, "Meal 1", 500, 100.0, "Chicken", 123,
                        "Fat", "g", 10.0),
                new MealFlatDTO(2L, date2, "Meal 2", 700, 150.0, "Rice", 234,
                        "Carb", "g", 30.0)
        );

        when(mealRepository.findDistinctDatesByMenuId(eq(USERNAME), eq(menuId), any(PageRequest.class)))
                .thenReturn(datesPage);
        when(mealRepository.findMealData(eq(USERNAME), eq(menuId), eq(List.of(date1, date2))))
                .thenReturn(flatData);

        Page<MealsByDate> result = menuService.getMenuMeals(USERNAME, menuId, page, size);

        assertEquals(2, result.getContent().size());
        MealsByDate first = result.getContent().get(0);
        MealsByDate second = result.getContent().get(1);

        assertEquals(date1, first.date());
        assertEquals(date2, second.date());
        assertEquals(1, first.meals().size());
        assertEquals(1, second.meals().size());

        MealDTO meal1 = first.meals().get(0);
        MealDTO meal2 = second.meals().get(0);

        assertEquals("Meal 1", meal1.name());
        assertEquals(500, meal1.targetKcal());
        assertEquals(1, meal2.mealItems().size());
        assertEquals("Rice", meal2.mealItems().get(0).product().name());

        verify(mealRepository).findDistinctDatesByMenuId(eq(USERNAME), eq(menuId), any(PageRequest.class));
        verify(mealRepository).findMealData(eq(USERNAME), eq(menuId), eq(List.of(date1, date2)));
    }

    @Test
    void shouldReturnEmptyPage_whenRepositoryReturnsNoDates() {
        long menuId = 1L;
        int page = 0, size = 2;

        Page<LocalDate> emptyDates = new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);

        when(mealRepository.findDistinctDatesByMenuId(eq(USERNAME), eq(menuId), any(PageRequest.class)))
                .thenReturn(emptyDates);

        Page<MealsByDate> result = menuService.getMenuMeals(USERNAME, menuId, page, size);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(emptyDates.getPageable(), result.getPageable());

        verify(mealRepository).findDistinctDatesByMenuId(eq(USERNAME), eq(menuId), any(PageRequest.class));
        verify(mealRepository, never()).findMealData(anyString(), anyLong(), anyList());
    }

    @Test
    void shouldAddMenuMeal_whenValidMenuId(){
        long menuId = 1L;
        MealRequest req = new MealRequest("Meal 1", 600, LocalDate.of(2025, 10, 10));
        Menu menu = new Menu();

        when(menuRepository.findByOwnerAndId(eq(USERNAME), eq(menuId))).thenReturn(Optional.of(menu));

        menuService.addMenuMeal(USERNAME, menuId, req);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        verify(menuMapper).toDto(any(Meal.class));

        Meal savedMeal = captor.getValue();
        assertEquals(req.name(), savedMeal.getName());
        assertEquals(req.targetKcal(), savedMeal.getTargetKcal());
        assertEquals(req.date(), savedMeal.getDate());
        assertSame(menu, savedMeal.getMenu());
        assertTrue(savedMeal.getMealItems().isEmpty());
    }

    @Test
    void shouldThrow_whenInvalidMenuId(){
        long menuId = 1L;
        MealRequest req = new MealRequest("Meal 1", 600, LocalDate.of(2025, 10, 10));

        when(menuRepository.findByOwnerAndId(eq(USERNAME), eq(menuId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuService.addMenuMeal(USERNAME, menuId, req));

        verify(mealRepository, never()).save(any(Meal.class));
        verify(menuMapper, never()).toDto(any(Meal.class));
    }
}