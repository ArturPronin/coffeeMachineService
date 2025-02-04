package test.example.coffeemachineservice.service;

import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;

import java.util.List;

public interface RecipeService {

    void addRecipe(AddNewRecipeRequestDto requestDto);

    List<RecipeResponseDto> getAllRecipes();

    String deleteRecipe(String recipeId);
}