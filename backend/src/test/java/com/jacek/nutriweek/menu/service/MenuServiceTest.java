package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.exception.MenuNotFoundException;
import com.jacek.nutriweek.menu.dto.MenuRequest;
import com.jacek.nutriweek.menu.entity.Menu;
import com.jacek.nutriweek.menu.mapper.MenuMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.MenuRepository;
import com.jacek.nutriweek.user.entity.User;
import com.jacek.nutriweek.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
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
}