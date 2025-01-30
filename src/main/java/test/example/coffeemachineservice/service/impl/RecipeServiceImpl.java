package test.example.coffeemachineservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.mapper.RecipeMapper;
import test.example.coffeemachineservice.persistent.entity.Ingredient;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;
import test.example.coffeemachineservice.persistent.repository.IngredientRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeIngredientRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeRepository;
import test.example.coffeemachineservice.service.RecipeService;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ALREADY_EXISTS_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    private final IngredientRepository ingredientRepository;

    private final RecipeMapper recipeMapper;

    @Override
    @Transactional
    public List<RecipeResponseDto> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            throw new RecipeException(HttpStatus.NOT_FOUND, "Рецепты не найдены");
        }

        return recipes.stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void addRecipe(AddNewRecipeRequestDto requestDto) {
        validateRecipeNotExists(requestDto.getRecipeName());

        Recipe newRecipe = recipeMapper.mapToRecipe(requestDto);
        newRecipe = recipeRepository.save(newRecipe);

        List<RecipeIngredient> recipeIngredients = createRecipeIngredients(requestDto, newRecipe);
        recipeIngredientRepository.saveAll(recipeIngredients);

        newRecipe.setRecipeIngredients(recipeIngredients);
    }

    private RecipeResponseDto convertToResponseDto(Recipe recipe) {
        RecipeResponseDto responseDto = recipeMapper.mapToRecipeResponseDto(recipe);

        if (recipe.getRecipeIngredients() != null && !recipe.getRecipeIngredients().isEmpty()) {
            List<RecipeIngredientDto> ingredients = recipe.getRecipeIngredients().stream()
                    .filter(ri -> ri.getIngredient() != null)
                    .map(recipeMapper::mapToRecipeIngredientDto)
                    .toList();
            responseDto.setRecipeIngredient(ingredients);
        } else {
            responseDto.setRecipeIngredient(List.of());
        }

        return responseDto;
    }

    private void validateRecipeNotExists(String recipeName) {
        if (recipeRepository.findByRecipeName(recipeName).isPresent()) {
            throw new RecipeException(RECIPE_ALREADY_EXISTS_MESSAGE);
        }
    }

    private List<RecipeIngredient> createRecipeIngredients(AddNewRecipeRequestDto requestDto, Recipe recipe) {
        return requestDto.getRecipeIngredients().stream()
                .map(dto -> createRecipeIngredient(dto, recipe))
                .toList();
    }

    private RecipeIngredient createRecipeIngredient(RecipeIngredientDto dto, Recipe recipe) {
        Ingredient ingredient = findIngredient(dto.getIngredientName());
        RecipeIngredient recipeIngredient = recipeMapper.mapToRecipeIngredient(dto, recipe);
        recipeIngredient.setIngredient(ingredient);
        return recipeIngredient;
    }

    private Ingredient findIngredient(String ingredientName) {
        return ingredientRepository.findByIngredientName(ingredientName)
                .orElseThrow(() -> new RecipeException(HttpStatus.NOT_FOUND, INGREDIENT_NOT_FOUND_MESSAGE));
    }
}
