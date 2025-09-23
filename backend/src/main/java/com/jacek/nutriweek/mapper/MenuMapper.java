package com.jacek.nutriweek.mapper;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.dto.MealItemDTO;
import com.jacek.nutriweek.dto.MenuDTO;
import com.jacek.nutriweek.dto.MenuResponseDTO;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.MealItem;
import com.jacek.nutriweek.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MenuMapper {

    Menu toEntity(MenuDTO menu);
    MenuResponseDTO toDto(Menu menu);

    @Mapping(target="id", source="id")
    MealDTO toDto(Meal meal);
    MealItemDTO toDto(MealItem mealItem);
}
