package com.jacek.nutriweek.menu.mapper;

import com.jacek.nutriweek.menu.dto.*;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.MealItem;
import com.jacek.nutriweek.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MenuMapper {

    @Mapping(target = "startDate", source = "startDate")
    Menu toEntity(MenuRequest menu);

    MenuResponse toDto(Menu menu);

    MealDTO toDto(Meal meal);
    MealItemDTO toDto(MealItem mealItem);


    default LocalDate map(String date){
        return date != null ? LocalDate.parse(date) : null;
    }



}
