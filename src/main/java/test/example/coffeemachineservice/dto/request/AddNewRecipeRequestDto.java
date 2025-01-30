package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность добавления нового рецепта")
public class AddNewRecipeRequestDto {

    @Schema(description = "Название", example = "Раф")
    String recipeName;

    @Schema(description = "Ингредиенты для рецепта")
    List<RecipeIngredientDto> recipeIngredients;
}
