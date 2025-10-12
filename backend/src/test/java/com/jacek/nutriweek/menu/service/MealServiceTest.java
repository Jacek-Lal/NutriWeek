package com.jacek.nutriweek.menu.service;

import com.jacek.nutriweek.common.exception.MealNotFoundException;
import com.jacek.nutriweek.menu.dto.MealItemDTO;
import com.jacek.nutriweek.menu.dto.NutrientDTO;
import com.jacek.nutriweek.menu.dto.ProductDTO;
import com.jacek.nutriweek.menu.entity.Meal;
import com.jacek.nutriweek.menu.entity.Nutrient;
import com.jacek.nutriweek.menu.entity.Product;
import com.jacek.nutriweek.menu.repository.MealRepository;
import com.jacek.nutriweek.menu.repository.NutrientRepository;
import com.jacek.nutriweek.menu.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MealServiceTest {
    @Mock MealRepository mealRepository;
    @Mock ProductRepository productRepository;
    @Mock NutrientRepository nutrientRepository;

    @InjectMocks MealService mealService;

    private final long MEAL_ID = 123L;
    @Test
    void shouldAddMealItemsForNewProductsAndNutrients(){
        Meal meal = createMeal();
        List<MealItemDTO> reqItems = createMealItemsRequest();

        List<Product> products = List.of(
                new Product("Broccoli", 111),
                new Product("Cheese", 222)
        );
        List<Nutrient> nutrients = List.of(
                new Nutrient("Water", "G"),
                new Nutrient("Carbohydrates", "G")
        );

        mockCommonRepositories(MEAL_ID, meal, products, nutrients);
        when(nutrientRepository.save(any(Nutrient.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        mealService.updateMealItems(MEAL_ID, reqItems);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        Meal updatedMeal = captor.getValue();

        verify(productRepository, times(2)).save(any(Product.class));
        verify(nutrientRepository, times(3)).save(any(Nutrient.class));

        ProductDTO expectedProduct1 = reqItems.get(0).product();
        ProductDTO expectedProduct2 = reqItems.get(1).product();
        Product actualProduct1 = updatedMeal.getMealItems().get(0).getProduct();
        Product actualProduct2 = updatedMeal.getMealItems().get(1).getProduct();

        assertEquals(reqItems.size() , meal.getMealItems().size());
        assertEquals(expectedProduct1.fdcId(), actualProduct1.getFdcId());
        assertEquals(expectedProduct2.fdcId(), actualProduct2.getFdcId());
    }
    @Test
    void shouldAddMealItemsForNewProducts_whenNutrientsExist(){
        Meal meal = createMeal();
        List<MealItemDTO> reqItems = createMealItemsRequest();

        List<Product> products = List.of(
                new Product("Broccoli", 111),
                new Product("Cheese", 222)
        );
        List<Nutrient> nutrients = List.of(
                new Nutrient("Protein", "G"),
                new Nutrient("Fat", "G"),
                new Nutrient("Energy", "kcal")
        );

        mockCommonRepositories(MEAL_ID, meal, products, nutrients);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        mealService.updateMealItems(MEAL_ID, reqItems);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        Meal updatedMeal = captor.getValue();

        verify(productRepository, times(2)).save(any(Product.class));
        verify(nutrientRepository, never()).save(any(Nutrient.class));

        ProductDTO expectedProduct1 = reqItems.get(0).product();
        ProductDTO expectedProduct2 = reqItems.get(1).product();
        Product actualProduct1 = updatedMeal.getMealItems().get(0).getProduct();
        Product actualProduct2 = updatedMeal.getMealItems().get(1).getProduct();

        assertEquals(reqItems.size() , meal.getMealItems().size());
        assertEquals(expectedProduct1.fdcId(), actualProduct1.getFdcId());
        assertEquals(expectedProduct2.fdcId(), actualProduct2.getFdcId());
    }

    @Test
    void shouldAddMealItems_whenAllProductsAndNutrientsExist(){
        Meal meal = createMeal();
        List<MealItemDTO> reqItems = createMealItemsRequest();

        List<Product> products = List.of(
                new Product("Chicken", 123),
                new Product("Potatoes", 245)
        );
        List<Nutrient> nutrients = List.of(
                new Nutrient("Protein", "G"),
                new Nutrient("Fat", "G"),
                new Nutrient("Energy", "kcal")
        );

        mockCommonRepositories(MEAL_ID, meal, products, nutrients);

        mealService.updateMealItems(MEAL_ID, reqItems);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        Meal updatedMeal = captor.getValue();

        verify(productRepository, never()).save(any(Product.class));
        verify(nutrientRepository, never()).save(any(Nutrient.class));

        ProductDTO expectedProduct1 = reqItems.get(0).product();
        ProductDTO expectedProduct2 = reqItems.get(1).product();
        Product actualProduct1 = updatedMeal.getMealItems().get(0).getProduct();
        Product actualProduct2 = updatedMeal.getMealItems().get(1).getProduct();

        assertEquals(reqItems.size() , meal.getMealItems().size());
        assertEquals(expectedProduct1.fdcId(), actualProduct1.getFdcId());
        assertEquals(expectedProduct2.fdcId(), actualProduct2.getFdcId());
    }
    @Test
    void shouldAddMealItems_whenSomeProductsAndNutrientsExist(){
        Meal meal = createMeal();
        List<MealItemDTO> reqItems = createMealItemsRequest();

        List<Product> products = List.of(
                new Product("Lettuce", 111),
                new Product("Potatoes", 245)
        );
        List<Nutrient> nutrients = List.of(
                new Nutrient("Protein", "G"),
                new Nutrient("Fat", "G"),
                new Nutrient("Energy", "kcal")
        );

        mockCommonRepositories(MEAL_ID, meal, products, nutrients);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        mealService.updateMealItems(MEAL_ID, reqItems);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        Meal updatedMeal = captor.getValue();

        verify(productRepository, times(1)).save(any(Product.class));
        verify(nutrientRepository, never()).save(any(Nutrient.class));

        ProductDTO expectedProduct1 = reqItems.get(0).product();
        ProductDTO expectedProduct2 = reqItems.get(1).product();
        Product actualProduct1 = updatedMeal.getMealItems().get(0).getProduct();
        Product actualProduct2 = updatedMeal.getMealItems().get(1).getProduct();

        assertEquals(reqItems.size() , meal.getMealItems().size());
        assertEquals(expectedProduct1.fdcId(), actualProduct1.getFdcId());
        assertEquals(expectedProduct2.fdcId(), actualProduct2.getFdcId());
    }

    @Test
    void shouldClearMealItems_whenRequestEmpty(){
        Meal meal = createMeal();
        List<MealItemDTO> reqItems = Collections.emptyList();

        when(mealRepository.findById(eq(MEAL_ID))).thenReturn(Optional.of(meal));

        mealService.updateMealItems(MEAL_ID, reqItems);

        verify(mealRepository).save(argThat(m -> m.getMealItems().isEmpty()));
        verifyNoInteractions(nutrientRepository, productRepository);

    }
    @Test
    void shouldThrow_whenInvalidMealId(){

        when(mealRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> mealService.updateMealItems(MEAL_ID, Collections.emptyList()));

        verify(mealRepository).findById(MEAL_ID);
        verifyNoMoreInteractions(mealRepository);
        verifyNoInteractions(productRepository, nutrientRepository);
    }


    private Meal createMeal(){
        return new Meal("Meal 1", 600, LocalDate.now(), null);
    }
    private List<MealItemDTO> createMealItemsRequest(){
        return List.of(
                new MealItemDTO(
                        new ProductDTO(123, "Chicken", List.of(
                                new NutrientDTO("Protein", "G", 20),
                                new NutrientDTO("Fat", "G", 5),
                                new NutrientDTO("Energy", "kcal", 150)

                        )),
                        100),
                new MealItemDTO(
                        new ProductDTO(245, "Potatoes", List.of(
                                new NutrientDTO("Protein", "G", 9),
                                new NutrientDTO("Fat", "G", 2),
                                new NutrientDTO("Energy", "kcal", 100)

                        )),
                        150)
        );
    }
    private void mockCommonRepositories(long mealId, Meal meal, List<Product> products, List<Nutrient> nutrients){
        when(mealRepository.findById(eq(mealId))).thenReturn(Optional.of(meal));
        when(productRepository.findAllByFdcIdIn(anySet())).thenReturn(products);
        when(nutrientRepository.findAll()).thenReturn(nutrients);
        when(mealRepository.save(any(Meal.class))).thenAnswer(inv -> inv.getArgument(0));
    }
}