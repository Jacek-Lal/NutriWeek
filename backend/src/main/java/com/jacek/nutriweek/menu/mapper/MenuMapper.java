package com.jacek.nutriweek.menu.mapper;

import com.jacek.nutriweek.menu.dto.MealDTO;
import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.MenuRequest;
import com.jacek.nutriweek.menu.dto.MenuResponse;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.MealItem;
import com.jacek.nutriweek.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MenuMapper {

    Menu toEntity(MenuRequest menu);
    MenuResponse toDto(Menu menu);

    MealDTO toDto(Meal meal);
    MealItemDTO toDto(MealItem mealItem);
}
