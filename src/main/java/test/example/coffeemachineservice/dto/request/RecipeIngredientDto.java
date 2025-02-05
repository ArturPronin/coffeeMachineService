package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ингредиенты для рецепта")
public class RecipeIngredientDto {

    @Schema(description = "Название ингредиента", example = "Кофе")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String ingredientName;

    @Schema(description = "Количество, используемое в рецепте", example = "7")
    @NotNull(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    @Min(value = 0, message = MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE)
    private int quantityOnRecipe;
}
