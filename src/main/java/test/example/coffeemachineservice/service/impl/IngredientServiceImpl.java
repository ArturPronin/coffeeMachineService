package test.example.coffeemachineservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.exception.IngredientException;
import test.example.coffeemachineservice.mapper.IngredientMapper;
import test.example.coffeemachineservice.persistent.entity.Ingredient;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;
import test.example.coffeemachineservice.persistent.repository.IngredientRepository;
import test.example.coffeemachineservice.service.IngredientService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENTS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_ID_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    @Override
    public void addIngredient(AddNewIngredientRequestDto requestDto) {
        if (ingredientRepository.findByIngredientName(requestDto.getIngredientName()).isPresent()) {
            throw new IngredientException(INGREDIENT_ALREADY_EXISTS_MESSAGE);
        }
        Ingredient newIngredient = ingredientMapper.mapToIngredient(requestDto);
        ingredientRepository.save(newIngredient);
    }

    @Override
    public List<IngredientResponseDto> getAllIngredients() {
        List<Ingredient> foundIngredients = ingredientRepository.findAll();
        if (foundIngredients.isEmpty()) {
            throw new IngredientException(INGREDIENTS_NOT_FOUND_MESSAGE);
        }
        return foundIngredients.stream()
                .map(ingredientMapper::mapToIngredientResponseDto)
                .toList();
    }

    @Override
    public void reduceIngredientQuantities(List<RecipeIngredient> recipeIngredients) {
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            ingredient.setAmountAvailable(ingredient.getAmountAvailable() - recipeIngredient.getQuantityOnRecipe());
            ingredientRepository.save(ingredient);
        }
    }

    @Override
    public boolean checkAmountIngredients(List<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream()
                .allMatch(recipeIngredient -> {
                    Ingredient ingredient = recipeIngredient.getIngredient();
                    int requiredQuantity = recipeIngredient.getQuantityOnRecipe();
                    int availableQuantity = ingredient.getAmountAvailable();
                    return availableQuantity - requiredQuantity >= 0;
                });
    }

    @Override
    public IngredientResponseDto updateAmountAvailableIngredient(UpdateIngredientRequestDto requestDto) {
        Ingredient foundIngredient = ingredientRepository.findByIngredientName(requestDto.getIngredientName())
                .orElseThrow(() -> new IngredientException(NOT_FOUND, INGREDIENT_NOT_FOUND_MESSAGE));
        foundIngredient.setAmountAvailable(foundIngredient.getAmountAvailable() + requestDto.getAddingQuantity());
        ingredientRepository.save(foundIngredient);
        return ingredientMapper.mapToIngredientResponseDto(foundIngredient);
    }

    @Override
    public String deleteIngredient(String ingredientId) {
        Ingredient foundIngredient = ingredientRepository.findById(UUID.fromString(ingredientId))
                .orElseThrow(() -> new IngredientException(INGREDIENT_ID_NOT_FOUND_MESSAGE));
        ingredientRepository.delete(foundIngredient);
        return INGREDIENT_DELETED_MESSAGE;
    }
}