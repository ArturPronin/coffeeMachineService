package test.example.coffeemachineservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(target = "recipeId", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    Recipe mapToRecipe(AddNewRecipeRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(source = "dto.quantityOnRecipe", target = "quantityOnRecipe")
    RecipeIngredient mapToRecipeIngredient(RecipeIngredientDto dto, Recipe recipe);

    @Mapping(target = "recipe.recipeIngredients", ignore = true)
    RecipeResponseDto mapToRecipeResponseDto(Recipe recipe);

    @Mapping(source = "ingredient.ingredientName", target = "ingredientName")
    RecipeIngredientDto mapToRecipeIngredientDto(RecipeIngredient recipeIngredient);
}
