package com.jacek.nutriweek.mapper;

import com.jacek.nutriweek.dto.menu.MealDTO;
import com.jacek.nutriweek.dto.menu.MealItemDTO;
import com.jacek.nutriweek.dto.menu.MenuRequest;
import com.jacek.nutriweek.dto.menu.MenuResponse;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.MealItem;
import com.jacek.nutriweek.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MenuMapper {

    Menu toEntity(MenuRequest menu);
    MenuResponse toDto(Menu menu);

    @Mapping(target="id", source="id")
    MealDTO toDto(Meal meal);
    MealItemDTO toDto(MealItem mealItem);
}
