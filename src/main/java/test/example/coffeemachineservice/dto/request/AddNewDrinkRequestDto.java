package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность добавления нового напитка")
public class AddNewDrinkRequestDto {

    @Schema(description = "Название", example = "Раф")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String drinkName;

    @Schema(description = "Название рецепта", example = "Раф")
    @NotBlank(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String recipeName;
}