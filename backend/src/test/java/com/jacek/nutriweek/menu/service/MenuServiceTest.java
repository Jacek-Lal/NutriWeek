package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.exception.MenuNotFoundException;
import com.jacek.nutriweek.menu.dto.MealDTO;
import com.jacek.nutriweek.menu.dto.MealRequest;
import com.jacek.nutriweek.menu.dto.MealsByDate;
import com.jacek.nutriweek.menu.dto.MenuRequest;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Menu;
import com.jacek.nutriweek.menu.mapper.MenuMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.MenuRepository;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock MenuRepository menuRepository;
    @Mock MealRepository mealRepository;
    @Mock UserRepository userRepository;
    @Mock MenuMapper menuMapper;

    @InjectMocks MenuService menuService;

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

        assertThrows(UsernameNotFoundException.class, () -> menuService.addMenu(USERNAME, req));

        verify(menuMapper, never()).toEntity(req);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void shouldReturnMenuResponse_whenValidId(){
        long id = 0;
        Menu menu = new Menu();

        when(menuRepository.findById(eq(id))).thenReturn(Optional.of(menu));

        menuService.getMenu(id);

        verify(menuMapper).toDto(menu);
    }

    @Test
    void shouldThrow_whenInvalidId(){
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class, () -> menuService.getMenu(0));

        verify(menuMapper, never()).toDto(any(Menu.class));
    }

    @Test
    void shouldReturnMealsByDate_whenRepositoryReturnsMultipleDates() {
        long menuId = 1L;
        int page = 0, size = 2;

        LocalDate date1 = LocalDate.of(2025, 10, 1);
        LocalDate date2 = LocalDate.of(2025, 10, 2);
        Page<LocalDate> datesPage = new PageImpl<>(List.of(date1, date2), PageRequest.of(page, size), 2);

        Meal meal1 = new Meal("Meal 1", 500, date1, null);
        Meal meal2 = new Meal("Meal 2", 700, date2, null);

        MealDTO dto1 = new MealDTO(1L,"Meal 1", 500, date1, List.of());
        MealDTO dto2 = new MealDTO(2L, "Meal 2", 700, date2, List.of());

        when(mealRepository.findDistinctDatesByMenuId(eq(menuId), any(PageRequest.class)))
                .thenReturn(datesPage);
        when(mealRepository.findByMenuIdAndDate(menuId, date1))
                .thenReturn(List.of(meal1));
        when(mealRepository.findByMenuIdAndDate(menuId, date2))
                .thenReturn(List.of(meal2));

        when(menuMapper.toDto(meal1)).thenReturn(dto1);
        when(menuMapper.toDto(meal2)).thenReturn(dto2);

        Page<MealsByDate> result = menuService.getMenuMeals(menuId, page, size);

        assertEquals(2, result.getContent().size());
        assertEquals(date1, result.getContent().get(0).date());
        assertEquals(date2, result.getContent().get(1).date());

        assertEquals(List.of(dto1), result.getContent().get(0).meals());
        assertEquals(List.of(dto2), result.getContent().get(1).meals());

        assertEquals(datesPage.getTotalElements(), result.getTotalElements());
        assertEquals(datesPage.getPageable(), result.getPageable());

        verify(mealRepository).findDistinctDatesByMenuId(eq(menuId), any(PageRequest.class));
        verify(mealRepository).findByMenuIdAndDate(menuId, date1);
        verify(mealRepository).findByMenuIdAndDate(menuId, date2);
        verify(menuMapper).toDto(meal1);
        verify(menuMapper).toDto(meal2);
    }

    @Test
    void shouldReturnMealsByDateWithEmptyMealList_whenNoMealsFoundForDate(){
        long menuId = 1L;
        int page = 0, size = 2;

        LocalDate date1 = LocalDate.of(2025, 10, 1);
        LocalDate date2 = LocalDate.of(2025, 10, 2);
        Page<LocalDate> datesPage = new PageImpl<>(List.of(date1, date2), PageRequest.of(page, size), 2);

        Meal meal1 = new Meal("Meal 1", 500, date1, null);

        MealDTO dto1 = new MealDTO(1L,"Meal 1", 500, date1, List.of());

        when(mealRepository.findDistinctDatesByMenuId(eq(menuId), any(PageRequest.class)))
                .thenReturn(datesPage);
        when(mealRepository.findByMenuIdAndDate(menuId, date1))
                .thenReturn(List.of(meal1));

        when(menuMapper.toDto(meal1)).thenReturn(dto1);

        Page<MealsByDate> result = menuService.getMenuMeals(menuId, page, size);

        assertEquals(2, result.getContent().size());
        assertEquals(date1, result.getContent().get(0).date());
        assertEquals(date2, result.getContent().get(1).date());

        assertEquals(List.of(dto1), result.getContent().get(0).meals());
        assertTrue(result.getContent().get(1).meals().isEmpty());
    }

    @Test
    void shouldReturnEmptyPage_whenRepositoryReturnsNoDates(){
        long menuId = 1L;
        int page = 0, size = 2;

        Page<LocalDate> datesPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);

        when(mealRepository.findDistinctDatesByMenuId(eq(menuId), any(PageRequest.class)))
                .thenReturn(datesPage);

        Page<MealsByDate> result = menuService.getMenuMeals(menuId, page, size);

        assertTrue(result.isEmpty());
        assertEquals(datesPage.getTotalElements(), result.getTotalElements());
        assertEquals(datesPage.getPageable(), result.getPageable());

        verify(mealRepository).findDistinctDatesByMenuId(eq(menuId), any(PageRequest.class));
        verify(mealRepository, never()).findByMenuIdAndDate(eq(menuId), any(LocalDate.class));
        verify(menuMapper, never()).toDto(any(Meal.class));
    }

    @Test
    void shouldAddMenuMeal_whenValidMenuId(){
        long menuId = 1L;
        MealRequest req = new MealRequest("Meal 1", 600, LocalDate.of(2025, 10, 10));
        Menu menu = new Menu();

        when(menuRepository.findById(eq(menuId))).thenReturn(Optional.of(menu));

        menuService.addMenuMeal(menuId, req);

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

        when(menuRepository.findById(eq(menuId))).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class, () -> menuService.addMenuMeal(menuId, req));

        verify(mealRepository, never()).save(any(Meal.class));
        verify(menuMapper, never()).toDto(any(Meal.class));
    }
}