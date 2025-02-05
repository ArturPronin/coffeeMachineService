package test.example.coffeemachineservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.mapper.RecipeMapper;
import test.example.coffeemachineservice.persistent.entity.Ingredient;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;
import test.example.coffeemachineservice.persistent.repository.IngredientRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeIngredientRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeRepository;
import test.example.coffeemachineservice.service.impl.RecipeServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_POSSIBLE_DELETE_RECIPE_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPES_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ID_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_MILK_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_RAF_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewRecipeRequestDto;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private final UUID recipeId = UUID.randomUUID();


    @Test
    void givenRecipesExist_whenGetAllRecipes_thenReturnList() {
        Recipe recipe1 = Recipe.builder().build();
        Recipe recipe2 = Recipe.builder().build();
        List<Recipe> recipes = List.of(recipe1, recipe2);

        RecipeResponseDto dto1 = RecipeResponseDto.builder().build();
        RecipeResponseDto dto2 = RecipeResponseDto.builder().build();

        when(recipeRepository.findAll()).thenReturn(recipes);
        when(recipeMapper.mapToRecipeResponseDto(any(Recipe.class))).thenReturn(dto1, dto2);

        List<RecipeResponseDto> result = recipeService.getAllRecipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recipeRepository).findAll();
        verify(recipeMapper, times(2)).mapToRecipeResponseDto(any(Recipe.class));
    }

    @Test
    void givenNoRecipes_whenGetAllRecipes_thenThrowRecipeException() {
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.getAllRecipes());
        assertEquals(RECIPES_NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(NOT_FOUND, exception.getStatus());
        verify(recipeRepository).findAll();
        verifyNoInteractions(recipeMapper);
    }

    @Test
    void givenRecipeWithExistingName_whenAddRecipe_thenThrowRecipeException() {
        AddNewRecipeRequestDto requestDto = createAddNewRecipeRequestDto();

        when(recipeRepository.findByRecipeName(requestDto.getRecipeName()))
                .thenReturn(Optional.of(Recipe.builder().build()));

        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.addRecipe(requestDto));
        assertEquals(RECIPE_ALREADY_EXISTS_MESSAGE, exception.getMessage());

        verify(recipeRepository).findByRecipeName(requestDto.getRecipeName());
        verifyNoMoreInteractions(recipeRepository, ingredientRepository, recipeMapper, recipeIngredientRepository);
    }

    @Test
    void givenNewRecipeAndAllIngredientsFound_whenAddRecipe_thenSavesRecipeAndIngredients() {
        Ingredient ingredient = Ingredient.builder().build();
        AddNewRecipeRequestDto requestDto = createAddNewRecipeRequestDto();
        when(recipeRepository.findByRecipeName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());

        Recipe newRecipe = Recipe.builder().build();
        when(recipeMapper.mapToRecipe(requestDto)).thenReturn(newRecipe);
        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME))
                .thenReturn(Optional.of(ingredient));

        RecipeIngredient recipeIngredient = RecipeIngredient.builder().build();
        when(recipeMapper.mapToRecipeIngredient(any(), eq(newRecipe), eq(ingredient)))
                .thenReturn(recipeIngredient);

        recipeService.addRecipe(requestDto);
        verify(recipeRepository).findByRecipeName(COFFEE_RAF_NAME);
        verify(recipeMapper).mapToRecipe(requestDto);
        verify(ingredientRepository).findByIngredientName(COFFEE_MILK_NAME);
        verify(recipeMapper).mapToRecipeIngredient(any(), eq(newRecipe), eq(ingredient));
        verify(recipeRepository).save(newRecipe);
        verify(recipeIngredientRepository).saveAll(List.of(recipeIngredient));
    }

    @Test
    void givenMissingIngredient_whenAddRecipe_thenThrowRecipeException() {
        AddNewRecipeRequestDto requestDto = createAddNewRecipeRequestDto();

        when(recipeRepository.findByRecipeName(COFFEE_RAF_NAME)).thenReturn(Optional.empty());

        Recipe newRecipe = Recipe.builder().recipeId(UUID.randomUUID()).recipeName(COFFEE_RAF_NAME).build();
        when(recipeMapper.mapToRecipe(requestDto)).thenReturn(newRecipe);

        when(ingredientRepository.findByIngredientName(COFFEE_MILK_NAME))
                .thenReturn(Optional.empty());

        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.addRecipe(requestDto));
        assertEquals(INGREDIENT_NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(NOT_FOUND, exception.getStatus());

        verify(recipeRepository).findByRecipeName(COFFEE_RAF_NAME);
        verify(recipeMapper).mapToRecipe(requestDto);
        verify(ingredientRepository).findByIngredientName(COFFEE_MILK_NAME);
        verifyNoMoreInteractions(recipeIngredientRepository);
    }

    @Test
    void givenExistingRecipeId_whenDeleteRecipe_thenReturnsDeletedMessage() {
        Recipe existingRecipe = Recipe.builder().build();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
        String result = recipeService.deleteRecipe(recipeId.toString());

        assertNotNull(result);
        assertEquals(RECIPE_DELETED_MESSAGE, result);

        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).delete(existingRecipe);
    }

    @Test
    void givenNonExistingRecipeId_whenDeleteRecipe_thenThrowRecipeException() {
        String stringRecipeId = String.valueOf(recipeId);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.deleteRecipe(stringRecipeId));

        assertEquals(RECIPE_ID_NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(NOT_FOUND, exception.getStatus());

        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository, never()).delete(any(Recipe.class));
    }

    @Test
    void givenNotPossibleDeleteRecipeId_whenDeleteRecipe_thenReturnsErrorMessage() {
        Recipe foundRecipe = Recipe.builder().build();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(foundRecipe));
        doThrow(new DataIntegrityViolationException(NOT_POSSIBLE_DELETE_RECIPE_MESSAGE))
                .when(recipeRepository).delete(foundRecipe);

        String result = recipeService.deleteRecipe(String.valueOf(recipeId));

        assertEquals(NOT_POSSIBLE_DELETE_RECIPE_MESSAGE, result);
        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).delete(foundRecipe);
    }
}