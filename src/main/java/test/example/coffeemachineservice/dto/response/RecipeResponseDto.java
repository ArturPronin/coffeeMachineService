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
@Schema(description = "Сущность отображения всех рецептов")
public class RecipeResponseDto {

    private String recipeId;

    private String recipeName;

    private List<RecipeIngredientDto> recipeIngredient;
}
