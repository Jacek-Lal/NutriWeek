package com.jacek.nutriweek.mapper;

import com.jacek.nutriweek.dto.MealDTO;
import com.jacek.nutriweek.dto.NutrientDTO;
import com.jacek.nutriweek.dto.ProductDTO;
import com.jacek.nutriweek.model.Meal;
import com.jacek.nutriweek.model.Nutrient;
import com.jacek.nutriweek.model.Product;
import com.jacek.nutriweek.model.ProductNutrient;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MealMapper {
    Meal toEntity(MealDTO dto);
    MealDTO toDto(Meal entity);

    default ProductNutrient toEntity(NutrientDTO nutrientDto, Product product){
        Nutrient nutrient = new Nutrient(nutrientDto.name(), nutrientDto.unit());

        return new ProductNutrient(nutrientDto.value(), product, nutrient);
    }


    @AfterMapping
    default void linkProductNutrients(@MappingTarget Product product, ProductDTO productDto){
        product.setNutrients(productDto
                .nutrients()
                .stream()
                .map(n -> toEntity(n, product))
                .toList());
    }

}
