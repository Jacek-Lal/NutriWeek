package com.jacek.nutriweek.mapper;

import com.jacek.nutriweek.dto.menu.NutrientDTO;
import com.jacek.nutriweek.dto.menu.ProductDTO;
import com.jacek.nutriweek.model.Product;
import com.jacek.nutriweek.model.ProductNutrient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product product);

    @Mapping(target = "name", source = "nutrient.name")
    @Mapping(target = "unit", source = "nutrient.unit")
    @Mapping(target = "value", source = "amount")
    NutrientDTO toDto(ProductNutrient pn);

    List<ProductDTO> toDtoList(List<Product> products);
    List<NutrientDTO> toNutrientDtoList(List<ProductNutrient> nutrients);
}
