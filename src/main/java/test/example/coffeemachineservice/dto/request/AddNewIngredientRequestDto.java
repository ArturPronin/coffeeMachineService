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
@Schema(description = "Сущность добавления нового ингредиента")
public class AddNewIngredientRequestDto {

    @Schema(description = "Название", example = "Сливки")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String ingredientName;

    @Schema(description = "Доступное количество", example = "100")
    @NotNull(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    @Min(value = 0, message = MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE)
    private int amountAvailable;

    @Schema(description = "Мерные единицы", example = "мл")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String unit;
}