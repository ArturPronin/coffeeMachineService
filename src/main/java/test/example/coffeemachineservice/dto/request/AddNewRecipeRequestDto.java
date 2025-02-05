package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность добавления нового рецепта")
public class AddNewRecipeRequestDto {

    @Schema(description = "Название", example = "Раф")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String recipeName;

    @Schema(description = "Ингредиенты для рецепта")
    @Valid
    private List<RecipeIngredientDto> recipeIngredients;
}