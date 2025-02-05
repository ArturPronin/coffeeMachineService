package test.example.coffeemachineservice.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.persistent.entity.Ingredient;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_MILK_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_RAF_NAME;

@SpringBootTest
class RecipeMapperTest {

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    void givenRecipeEntity_whenMapToRecipeResponseDto_thenSuccess() {
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(Ingredient.builder()
                        .ingredientName(COFFEE_MILK_NAME)
                        .build())
                .build();

        List<RecipeIngredient> listRecipeIngredient = new ArrayList<>();
        listRecipeIngredient.add(recipeIngredient);

        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName(COFFEE_RAF_NAME)
                .recipeIngredients(listRecipeIngredient)
                .build();


        RecipeResponseDto responseDto = recipeMapper.mapToRecipeResponseDto(recipe);

        assertNotNull(responseDto);
        assertEquals(recipe.getRecipeId(), UUID.fromString(responseDto.getRecipeId()));
        assertEquals(recipe.getRecipeName(), responseDto.getRecipeName());
        assertEquals(
                recipe.getRecipeIngredients().get(0).getIngredient().getIngredientName(),
                responseDto.getRecipeIngredient().get(0).getIngredientName()
        );
    }

    @Test
    void givenNullRecipeEntity_whenMapToRecipeResponseDto_thenReturnsNull() {
        Recipe recipe = null;
        RecipeResponseDto responseDto = recipeMapper.mapToRecipeResponseDto(recipe);
        assertNull(responseDto);
    }

    @Test
    void givenRecipeIngredientEntity_whenMapToRecipeIngredientDto_thenSuccess() {
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(Ingredient.builder()
                        .ingredientName(COFFEE_MILK_NAME)
                        .build())
                .build();

        RecipeIngredientDto responseDto = recipeMapper.mapToRecipeIngredientDto(recipeIngredient);

        assertNotNull(responseDto);
        assertEquals(recipeIngredient.getIngredient().getIngredientName(), responseDto.getIngredientName());
    }

    @Test
    void givenNullRecipeIngredientEntity_whenMapToRecipeIngredientDto_thenReturnsNull() {
        RecipeIngredient recipeIngredient = null;
        RecipeIngredientDto responseDto = recipeMapper.mapToRecipeIngredientDto(recipeIngredient);
        assertNull(responseDto);
    }

    @Test
    void givenRecipeIngredientDtoAndRecipeAndIngredient_whenMapToRecipeIngredient_thenSuccess() {
        RecipeIngredientDto recipeIngredientDto = RecipeIngredientDto.builder()
                .ingredientName(COFFEE_MILK_NAME)
                .quantityOnRecipe(0)
                .build();

        Recipe recipe = Recipe.builder()
                .recipeName(COFFEE_RAF_NAME)
                .build();

        Ingredient ingredient = Ingredient.builder()
                .ingredientName(recipeIngredientDto.getIngredientName())
                .build();

        RecipeIngredient recipeIngredient = recipeMapper.mapToRecipeIngredient(recipeIngredientDto, recipe, ingredient);

        assertNotNull(recipeIngredient);
        assertEquals(recipeIngredientDto.getIngredientName(), recipeIngredient.getIngredient().getIngredientName());
        assertEquals(recipeIngredientDto.getQuantityOnRecipe(), recipeIngredient.getQuantityOnRecipe());
        assertEquals(recipe.getRecipeName(), recipeIngredient.getRecipe().getRecipeName());
    }

    @Test
    void givenNullRecipeIngredientDtoAndRecipeAndIngredient_whenMapToRecipeIngredient_thenReturnsNull() {
        RecipeIngredientDto recipeIngredientDto = null;
        Recipe recipe = null;
        Ingredient ingredient = null;
        RecipeIngredient recipeIngredient = recipeMapper.mapToRecipeIngredient(recipeIngredientDto, recipe, ingredient);
        assertNull(recipeIngredient);
    }

    @Test
    void givenAddNewRecipeRequestDto_whenMapToRecipe_thenSuccess() {
        RecipeIngredientDto recipeIngredientDto = RecipeIngredientDto.builder().build();

        List<RecipeIngredientDto> listRecipeIngredientDto = new ArrayList<>();
        listRecipeIngredientDto.add(recipeIngredientDto);

        AddNewRecipeRequestDto requestDto = AddNewRecipeRequestDto.builder()
                .recipeName(COFFEE_RAF_NAME)
                .recipeIngredients(listRecipeIngredientDto)
                .build();

        Recipe recipe = recipeMapper.mapToRecipe(requestDto);

        assertNotNull(recipe);
        assertEquals(requestDto.getRecipeName(), recipe.getRecipeName());
    }

    @Test
    void givenNullAddNewRecipeRequestDto_whenMapToRecipe_thenReturnsNull() {
        AddNewRecipeRequestDto requestDto = null;
        Recipe recipe = recipeMapper.mapToRecipe(requestDto);
        assertNull(recipe);
    }
}