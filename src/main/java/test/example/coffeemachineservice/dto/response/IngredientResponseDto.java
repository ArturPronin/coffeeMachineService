package test.example.coffeemachineservice.dto.response;

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
@Schema(description = "Сущность отображения ингредиента")
public class IngredientResponseDto {

    @Schema(description = "ingredientId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
    private String ingredientId;

    @Schema(description = "ingredientName", example = "Сливки")
    private String ingredientName;

    @Schema(description = "amountAvailable", example = "100")
    private int amountAvailable;

    @Schema(description = "unit", example = "мл")
    private String unit;
}