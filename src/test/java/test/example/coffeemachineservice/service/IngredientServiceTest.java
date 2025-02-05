package test.example.coffeemachineservice.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.exception.IngredientException;
import test.example.coffeemachineservice.mapper.IngredientMapper;
import test.example.coffeemachineservice.persistent.entity.Ingredient;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;
import test.example.coffeemachineservice.persistent.repository.IngredientRepository;
import test.example.coffeemachineservice.service.impl.IngredientServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_POSSIBLE_DELETE_INGREDIENT_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_MILK_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.TEST_UUID;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewIngredientRequestDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createUpdateIngredientRequestDto;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @Test
    void givenValidAddNewIngredientRequestDto_whenAddIngredient_thenReturnsSuccess() {
        AddNewIngredientRequestDto requestDto = createAddNewIngredientRequestDto();
        Ingredient ingredient = Ingredient.builder().build();

        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME)).thenReturn(Optional.empty());
        when(ingredientMapper.mapToIngredient(requestDto)).thenReturn(ingredient);

        ingredientService.addIngredient(requestDto);

        verify(ingredientRepository).save(ingredient);
        verify(ingredientRepository).findByIngredientName(COFFEE_MILK_NAME);
        verify(ingredientMapper).mapToIngredient(requestDto);
    }

    @Test
    void givenExistsIngredient_whenAddIngredient_thenReturnsBadRequest() {
        AddNewIngredientRequestDto requestDto = createAddNewIngredientRequestDto();
        requestDto.setIngredientName(COFFEE_MILK_NAME);

        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME))
                .thenReturn(Optional.of(Ingredient.builder().build()));

        assertThrows(IngredientException.class, () -> ingredientService.addIngredient(requestDto));
        verify(ingredientRepository).findByIngredientName(COFFEE_MILK_NAME);
        verifyNoMoreInteractions(ingredientMapper, ingredientRepository);
    }

    @Test
    void givenExistingIngredients_whenGetAllIngredients_thenReturnsSuccess() {
        List<Ingredient> ingredients = List.of(
                Ingredient.builder().build(),
                Ingredient.builder().build()
        );
        List<IngredientResponseDto> expectedResponse = List.of(
                IngredientResponseDto.builder().build(),
                IngredientResponseDto.builder().build()
        );

        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(ingredientMapper.mapToIngredientResponseDto(any())).thenReturn(IngredientResponseDto.builder().build());

        List<IngredientResponseDto> result = ingredientService.getAllIngredients();

        assertEquals(expectedResponse.size(), result.size());
        verify(ingredientRepository).findAll();
        verify(ingredientMapper, times(2)).mapToIngredientResponseDto(any());
    }

    @Test
    void givenNonExistingIngredients_whenGetAllIngredients_thenReturnsNotFound() {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(IngredientException.class, () -> ingredientService.getAllIngredients());
        verify(ingredientRepository).findAll();
        verifyNoInteractions(ingredientMapper);
    }

    @Test
    void givenValidRecipeIngredients_whenReduceIngredientQuantities_thenReturnsSuccess() {
        Ingredient ingredient1 = Ingredient.builder()
                .amountAvailable(100)
                .build();
        Ingredient ingredient2 = Ingredient.builder()
                .amountAvailable(200)
                .build();

        RecipeIngredient recipeIngredient1 = RecipeIngredient.builder()
                .ingredient(ingredient1)
                .quantityOnRecipe(50)
                .build();
        RecipeIngredient recipeIngredient2 = RecipeIngredient.builder()
                .ingredient(ingredient2)
                .quantityOnRecipe(75)
                .build();

        List<RecipeIngredient> recipeIngredients = List.of(recipeIngredient1, recipeIngredient2);

        ingredientService.reduceIngredientQuantities(recipeIngredients);

        assertEquals(50, ingredient1.getAmountAvailable());
        assertEquals(125, ingredient2.getAmountAvailable());
        verify(ingredientRepository, times(2)).save(any(Ingredient.class));
    }

    @Test
    void givenSufficientIngredients_whenCheckAmountIngredients_thenReturnsTrue() {
        Ingredient ingredient1 = Ingredient.builder()
                .amountAvailable(100)
                .build();
        RecipeIngredient recipeIngredient1 = RecipeIngredient.builder()
                .ingredient(ingredient1)
                .quantityOnRecipe(50)
                .build();

        List<RecipeIngredient> recipeIngredients = List.of(recipeIngredient1);

        boolean result = ingredientService.checkAmountIngredients(recipeIngredients);

        assertTrue(result);
    }

    @Test
    void givenInsufficientIngredients_whenCheckAmountIngredients_thenReturnsFalse() {
        Ingredient ingredient1 = Ingredient.builder()
                .amountAvailable(30)
                .build();
        RecipeIngredient recipeIngredient1 = RecipeIngredient.builder()
                .ingredient(ingredient1)
                .quantityOnRecipe(50)
                .build();

        List<RecipeIngredient> recipeIngredients = List.of(recipeIngredient1);

        boolean result = ingredientService.checkAmountIngredients(recipeIngredients);

        assertFalse(result);
    }

    @Test
    void givenValidUpdateRequest_whenUpdateAmountAvailableIngredient_thenReturnsSuccess() {
        UpdateIngredientRequestDto requestDto = createUpdateIngredientRequestDto();
        Ingredient ingredient = Ingredient.builder()
                .amountAvailable(100)
                .build();
        IngredientResponseDto expectedResponse = IngredientResponseDto.builder().build();

        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.mapToIngredientResponseDto(ingredient)).thenReturn(expectedResponse);

        IngredientResponseDto result = ingredientService.updateAmountAvailableIngredient(requestDto);

        assertEquals(expectedResponse, result);
        assertEquals(200, ingredient.getAmountAvailable());
        verify(ingredientRepository).save(ingredient);
        verify(ingredientMapper).mapToIngredientResponseDto(ingredient);
    }

    @Test
    void givenNonExistingIngredient_whenUpdateAmountAvailableIngredient_thenReturnsNotFound() {
        UpdateIngredientRequestDto requestDto = createUpdateIngredientRequestDto();

        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME)).thenReturn(Optional.empty());

        assertThrows(IngredientException.class,
                () -> ingredientService.updateAmountAvailableIngredient(requestDto));
        verify(ingredientRepository).findByIngredientName(COFFEE_MILK_NAME);
        verifyNoMoreInteractions(ingredientMapper, ingredientRepository);
    }

    @Test
    void givenValidIngredientId_whenDeleteIngredient_thenReturnsSuccess() {
        Ingredient ingredient = Ingredient.builder().build();
        when(ingredientRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.of(ingredient));

        String result = ingredientService.deleteIngredient(TEST_UUID);

        assertEquals(INGREDIENT_DELETED_MESSAGE, result);
        verify(ingredientRepository).findById(UUID.fromString(TEST_UUID));
        verify(ingredientRepository).delete(ingredient);
    }

    @Test
    void givenNonExistingIngredientId_whenDeleteIngredient_thenReturnsNotFound() {
        when(ingredientRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.empty());

        assertThrows(IngredientException.class, () -> ingredientService.deleteIngredient(TEST_UUID));
        verify(ingredientRepository).findById(UUID.fromString(TEST_UUID));
        verify(ingredientRepository, never()).delete(any());
    }

    @Test
    void givenNotPossibleDeleteIngredientId_whenDeleteIngredient_thenReturnsErrorMessage() {
        Ingredient foundIngredient = Ingredient.builder().build();

        when(ingredientRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.of(foundIngredient));

        doThrow(new DataIntegrityViolationException(NOT_POSSIBLE_DELETE_INGREDIENT_MESSAGE))
                .when(ingredientRepository).delete(foundIngredient);

        String result = ingredientService.deleteIngredient(TEST_UUID);

        assertEquals(NOT_POSSIBLE_DELETE_INGREDIENT_MESSAGE, result);
        verify(ingredientRepository).findById(UUID.fromString(TEST_UUID));
        verify(ingredientRepository).delete(foundIngredient);
    }
}