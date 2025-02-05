package test.example.coffeemachineservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность отображения рецепта")
public class RecipeResponseDto {

    @Schema(description = "recipeId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
    private String recipeId;

    @Schema(description = "recipeName", example = "Раф")
    private String recipeName;

    @Schema(description = "recipeIngredient")
    private List<RecipeIngredientDto> recipeIngredient;
}