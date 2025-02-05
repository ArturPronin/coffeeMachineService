package test.example.coffeemachineservice.service;

import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;

import java.util.List;

public interface IngredientService {

    void addIngredient(AddNewIngredientRequestDto requestDto);

    List<IngredientResponseDto> getAllIngredients();

    void reduceIngredientQuantities(List<RecipeIngredient> recipeIngredients);

    boolean checkAmountIngredients(List<RecipeIngredient> recipeIngredients);

    IngredientResponseDto updateAmountAvailableIngredient(UpdateIngredientRequestDto requestDto);

    String deleteIngredient(String ingredientId);
}