package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ингредиенты для рецепта")
public class RecipeIngredientDto {

    @Schema(description = "Название ингредиента", example = "Кофе")
    String ingredientName;

    @Schema(description = "Количество, используемое в рецепте", example = "7")
    Integer quantityOnRecipe;
}
