package test.example.coffeemachineservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.exception.DrinkException;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.mapper.DrinkMapper;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.repository.DrinkRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeRepository;
import test.example.coffeemachineservice.service.impl.DrinkServiceImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_POSSIBLE_DELETE_DRINKS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.WAIT_UNTIL_READY_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_RAF_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.TEST_UUID;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewDrinkRequestDto;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.COMPLETED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.CREATED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.PROGRESS;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.REFUSED;

@ExtendWith(MockitoExtension.class)
class DrinkServiceTest {
    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private DrinkMapper drinkMapper;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private DrinkServiceImpl drinkService;

    private final UUID orderId = UUID.randomUUID();

    @Test
    void givenValidAddNewDrinkRequestDto_whenAddDrink_thenReturnsSuccess() {
        AddNewDrinkRequestDto requestDto = createAddNewDrinkRequestDto();
        Drink drink = Drink.builder().build();
        Recipe recipe = Recipe.builder().build();

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());
        when(drinkMapper.mapToDrink(requestDto)).thenReturn(drink);
        when(recipeRepository.findByRecipeName(COFFEE_RAF_NAME)).thenReturn(Optional.of(recipe));

        drinkService.addDrink(requestDto);

        verify(drinkRepository).save(drink);
        verify(drinkRepository).findByDrinkName(COFFEE_RAF_NAME);
        verify(recipeRepository).findByRecipeName(COFFEE_RAF_NAME);
    }

    @Test
    void givenExistsDrink_whenAddDrink_thenReturnsBadRequest() {
        AddNewDrinkRequestDto requestDto = createAddNewDrinkRequestDto();
        requestDto.setDrinkName(COFFEE_RAF_NAME);

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.of(Drink.builder().build()));

        assertThrows(DrinkException.class, () -> drinkService.addDrink(requestDto));
        verify(drinkRepository).findByDrinkName(COFFEE_RAF_NAME);
        verifyNoMoreInteractions(drinkMapper, recipeRepository, drinkRepository);
    }

    @Test
    void givenNonExistingRecipe_whenAddDrink_thenReturnsNotFound() {
        AddNewDrinkRequestDto requestDto = createAddNewDrinkRequestDto();
        requestDto.setDrinkName(COFFEE_RAF_NAME);
        requestDto.setRecipeName(COFFEE_RAF_NAME);

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());
        when(drinkMapper.mapToDrink(requestDto)).thenReturn(Drink.builder().build());
        when(recipeRepository.findByRecipeName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());

        assertThrows(RecipeException.class, () -> drinkService.addDrink(requestDto));
    }

    @Test
    void givenExistingDrinks_whenGetAllDrinks_thenReturnsSuccess() {
        List<Drink> drinks = List.of(Drink.builder().build(), Drink.builder().build());
        List<DrinkResponseDto> expectedResponse = List.of(
                DrinkResponseDto.builder().build(), DrinkResponseDto.builder().build()
        );

        when(drinkRepository.findAll()).thenReturn(drinks);
        when(drinkMapper.mapToDrinkResponseDto(any())).thenReturn(DrinkResponseDto.builder().build());

        List<DrinkResponseDto> result = drinkService.getAllDrinks();

        assertEquals(expectedResponse.size(), result.size());
        verify(drinkRepository).findAll();
        verify(drinkMapper, times(2)).mapToDrinkResponseDto(any());
    }

    @Test
    void givenNonExistingDrinks_whenGetAllDrinks_thenReturnsNotFound() {
        when(drinkRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(DrinkException.class, () -> drinkService.getAllDrinks());
        verify(drinkRepository).findAll();
        verifyNoInteractions(drinkMapper);
    }

    @Test
    void givenValidDrinkRequest_whenMakeDrink_thenReturnsSuccess() {
        MakeDrinkRequestDto requestDto = MakeDrinkRequestDto.builder().build();
        requestDto.setDrinkName(COFFEE_RAF_NAME);

        Drink drink = Drink.builder().build();
        Recipe recipe = Recipe.builder().build();
        recipe.setRecipeIngredients(new ArrayList<>());
        drink.setRecipe(recipe);

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.of(drink));
        when(orderService.createOrder(any(), anyString())).thenReturn(orderId);
        when(ingredientService.checkAmountIngredients(anyList())).thenReturn(true);

        String result = drinkService.makeDrink(requestDto);

        assertEquals(WAIT_UNTIL_READY_MESSAGE, result);
        verify(drinkRepository).findByDrinkName(COFFEE_RAF_NAME);
        verify(orderService).createOrder(drink, CREATED.getStatusName());
        verify(ingredientService).checkAmountIngredients(anyList());
        verify(ingredientService).reduceIngredientQuantities(anyList());
        verify(orderService).updateOrderStatus(orderId, PROGRESS.getStatusName());
        verify(orderService).updateOrderStatusAfterDelay(eq(orderId), eq(COMPLETED), any(Duration.class));
        verify(drinkRepository).save(drink);
    }

    @Test
    void givenNonExistingDrink_whenMakeDrink_thenReturnsNotFound() {
        MakeDrinkRequestDto requestDto = MakeDrinkRequestDto.builder().build();
        requestDto.setDrinkName(COFFEE_RAF_NAME);

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());

        assertThrows(DrinkException.class, () -> drinkService.makeDrink(requestDto));
        verify(drinkRepository).findByDrinkName(COFFEE_RAF_NAME);
        verifyNoInteractions(orderService, ingredientService);
    }

    @Test
    void givenInsufficientIngredients_whenMakeDrink_thenReturnsBadRequest() {
        MakeDrinkRequestDto requestDto = MakeDrinkRequestDto.builder().build();
        requestDto.setDrinkName(COFFEE_RAF_NAME);

        Drink drink = Drink.builder().build();
        Recipe recipe = Recipe.builder().build();
        recipe.setRecipeIngredients(new ArrayList<>());
        drink.setRecipe(recipe);

        when(drinkRepository.findByDrinkName(COFFEE_RAF_NAME)).thenReturn(Optional.of(drink));
        when(orderService.createOrder(any(), anyString())).thenReturn(orderId);
        when(ingredientService.checkAmountIngredients(anyList())).thenReturn(false);

        assertThrows(DrinkException.class, () -> drinkService.makeDrink(requestDto));
        verify(orderService).updateOrderStatus(orderId, REFUSED.getStatusName());
    }

    @Test
    void givenExistingDrinks_whenGetPopularDrink_thenReturnsSuccess() {
        Drink drink = Drink.builder().build();
        DrinkResponseDto expectedResponse = DrinkResponseDto.builder().build();

        when(drinkRepository.findMostPopularDrink()).thenReturn(Optional.of(drink));
        when(drinkMapper.mapToDrinkResponseDto(drink)).thenReturn(expectedResponse);

        DrinkResponseDto result = drinkService.getPopularDrink();

        assertEquals(expectedResponse, result);
        verify(drinkRepository).findMostPopularDrink();
        verify(drinkMapper).mapToDrinkResponseDto(drink);
    }

    @Test
    void givenNonExistingDrinks_whenGetPopularDrink_thenReturnsNotFound() {
        when(drinkRepository.findMostPopularDrink()).thenReturn(Optional.empty());

        assertThrows(DrinkException.class, () -> drinkService.getPopularDrink());
        verify(drinkRepository).findMostPopularDrink();
        verifyNoInteractions(drinkMapper);
    }

    @Test
    void givenValidDrinkId_whenDeleteDrink_thenReturnsSuccess() {
        Drink drink = Drink.builder().build();
        when(drinkRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.of(drink));

        String result = drinkService.deleteDrink(TEST_UUID);

        assertEquals(DRINK_DELETED_MESSAGE, result);
        verify(drinkRepository).findById(UUID.fromString(TEST_UUID));
        verify(drinkRepository).delete(drink);
    }

    @Test
    void givenNonExistingDrinkId_whenDeleteDrink_thenReturnsNotFound() {
        when(drinkRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.empty());

        assertThrows(DrinkException.class, () -> drinkService.deleteDrink(TEST_UUID));
        verify(drinkRepository).findById(UUID.fromString(TEST_UUID));
        verify(drinkRepository, never()).delete(any());
    }

    @Test
    void givenNotPossibleDeleteDrinkId_whenDeleteDrink_thenReturnsErrorMessage() {
        Drink foundDrink = Drink.builder().build();

        when(drinkRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.of(foundDrink));

        doThrow(new DataIntegrityViolationException(NOT_POSSIBLE_DELETE_DRINKS_MESSAGE))
                .when(drinkRepository).delete(foundDrink);

        String result = drinkService.deleteDrink(TEST_UUID);

        assertEquals(NOT_POSSIBLE_DELETE_DRINKS_MESSAGE, result);
        verify(drinkRepository).findById(UUID.fromString(TEST_UUID));
        verify(drinkRepository).delete(foundDrink);
    }
}