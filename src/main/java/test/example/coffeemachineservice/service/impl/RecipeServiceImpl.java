package test.example.coffeemachineservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
import test.example.coffeemachineservice.service.RecipeService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_POSSIBLE_DELETE_RECIPE_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPES_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ID_NOT_FOUND_MESSAGE;

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
        log.info("Получение всех рецептов");
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            throw new RecipeException(NOT_FOUND, RECIPES_NOT_FOUND_MESSAGE);
        }
        return recipes.stream()
                .map(recipeMapper::mapToRecipeResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void addRecipe(AddNewRecipeRequestDto requestDto) {
        log.info("Добавление нового рецепта: {}", requestDto.getRecipeName());
        if (recipeRepository.findByRecipeName(requestDto.getRecipeName()).isPresent()) {
            throw new RecipeException(RECIPE_ALREADY_EXISTS_MESSAGE);
        }
        Recipe newRecipe = recipeMapper.mapToRecipe(requestDto);
        List<RecipeIngredient> recipeIngredients = requestDto.getRecipeIngredients().stream()
                .map(dto -> {
                    Ingredient ingredient = ingredientRepository.findByIngredientName(dto.getIngredientName())
                            .orElseThrow(() -> new RecipeException(NOT_FOUND, INGREDIENT_NOT_FOUND_MESSAGE));
                    return recipeMapper.mapToRecipeIngredient(dto, newRecipe, ingredient);
                })
                .toList();

        newRecipe.setRecipeIngredients(recipeIngredients);
        recipeRepository.save(newRecipe);
        recipeIngredientRepository.saveAll(recipeIngredients);
        log.info("Рецепт '{}' успешно добавлен", newRecipe.getRecipeName());
    }

    @Override
    public String deleteRecipe(String recipeId) {
        log.info("Удаление рецепта с ID {}", recipeId);
        Recipe foundRecipe = recipeRepository.findById(UUID.fromString(recipeId))
                .orElseThrow(() -> new RecipeException(NOT_FOUND, RECIPE_ID_NOT_FOUND_MESSAGE));
        try {
            recipeRepository.delete(foundRecipe);
            log.info("Рецепт с ID {} успешно удален", recipeId);
            return RECIPE_DELETED_MESSAGE;
        } catch (DataIntegrityViolationException exception) {
            log.error("Ошибка при удалении рецепта с ID {}: {}", recipeId, exception.getMessage());
            return NOT_POSSIBLE_DELETE_RECIPE_MESSAGE;
        }
    }
}