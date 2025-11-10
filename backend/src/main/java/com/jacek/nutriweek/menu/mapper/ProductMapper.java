package com.jacek.nutriweek.menu.mapper;

import com.jacek.nutriweek.menu.dto.NutrientDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.dto.usda.ApiNutrient;
import com.jacek.nutriweek.menu.dto.usda.ApiProduct;
import com.jacek.nutriweek.menu.entity.Product;
import com.jacek.nutriweek.menu.entity.ProductNutrient;
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

    List<ProductDTO> fromApiToProductDtoList(List<ApiProduct> products);
    List<NutrientDTO> fromApiToNutrientDtoList(List<ApiNutrient> nutrients);
}
