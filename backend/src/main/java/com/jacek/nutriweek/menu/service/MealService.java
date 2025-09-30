package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.menu.dto.MealDTO;
import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.NutrientDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.entity.*;
import com.jacek.nutriweek.menu.mapper.MealMapper;
import com.jacek.nutriweek.menu.mapper.ProductMapper;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.NutrientRepository;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;

    private final MealMapper mealMapper;
    private final ProductMapper productMapper;

    public Meal updateMealItems(Long mealId, List<MealItemDTO> items) {
        Meal meal =  mealRepository.findById(mealId).orElseThrow();
        meal.getMealItems().clear();

        Set<Integer> fdcIds = items.stream()
                .map(mi -> mi.product().fdcId())
                .collect(Collectors.toSet());

        Map<Integer, Product> existingProducts = productRepository.findAllByFdcIdIn(fdcIds)
                .stream()
                .collect(Collectors.toMap(Product::getFdcId, p -> p));

        Map<String, Nutrient> existingNutrients = nutrientRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nutrient::getKey, n -> n));


        List<MealItem> mealItems = new ArrayList<>(items.size());
        for (MealItemDTO reqItem : items){
            ProductDTO reqProduct = reqItem.product();
            Product product = existingProducts.get(reqProduct.fdcId());

            if(product == null){
                Product newProduct = new Product();
                newProduct.setName(reqProduct.name());
                newProduct.setFdcId(reqProduct.fdcId());

                List<ProductNutrient> pns = new ArrayList<>();
                for(NutrientDTO reqNut : reqProduct.nutrients()){
                    Nutrient newNut = new Nutrient(reqNut.name(), reqNut.unit());
                    String key = newNut.getKey();

                    Nutrient managedNut = existingNutrients.get(key);
                    if(managedNut == null){
                        managedNut = nutrientRepository.save(newNut);
                        existingNutrients.put(key, managedNut);
                    }

                    ProductNutrient pn = new ProductNutrient();
                    pn.setAmount(reqNut.value());
                    pn.setNutrient(managedNut);
                    pn.setProduct(newProduct);
                    pns.add(pn);
                }
                newProduct.setNutrients(pns);
                product = productRepository.save(newProduct);
                existingProducts.put(product.getFdcId(), product);
            }
            MealItem mi = new MealItem();
            mi.setAmount(reqItem.amount());
            mi.setProduct(product);
            mi.setMeal(meal);
            mealItems.add(mi);
        }
        meal.getMealItems().clear();
        meal.getMealItems().addAll(mealItems);

        return mealRepository.save(meal);
    }

    public Meal addMeal(MealDTO mealDto) {
        Meal meal = mealMapper.toEntity(mealDto);

        Set<Integer> fdcIds = meal.getMealItems().stream()
                .map(mi -> mi.getProduct().getFdcId())
                .collect(Collectors.toSet());

        Map<Integer, Product> existingProducts = productRepository.findAllByFdcIdIn(fdcIds)
                .stream()
                .collect(Collectors.toMap(Product::getFdcId, p -> p));

        Map<String, Nutrient> existingNutrients = nutrientRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nutrient::getKey, n -> n));


        for (MealItem mealItem : meal.getMealItems()){
            Product product = existingProducts.get(mealItem.getProduct().getFdcId());

            if(product == null)
                product = productRepository.save(mealItem.getProduct());

            for (ProductNutrient pn : product.getNutrients()){
                Nutrient nutrient = existingNutrients.get(pn.getNutrient().getKey());
                if(nutrient == null){
                    nutrient = nutrientRepository.save(pn.getNutrient());
                    existingNutrients.put(nutrient.getKey(), nutrient);
                }
                pn.setNutrient(nutrient);
                pn.setProduct(product);
            }
            mealItem.setProduct(product);

        }
        return mealRepository.save(meal);
    }

    public List<ProductDTO> getRecentProducts(int limit) {
        return productMapper.toDtoList(mealRepository.findRecentProducts(limit));
    }

    public void deleteMeal(Long mealId) {
        mealRepository.deleteById(mealId);
    }
}
