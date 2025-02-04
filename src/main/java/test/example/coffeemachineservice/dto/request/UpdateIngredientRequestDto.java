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
@Schema(description = "Сущность обновления ингредиента")
public class UpdateIngredientRequestDto {

    @Schema(description = "ingredientName", example = "Сливки")
    private String ingredientName;

    @Schema(description = "amountAvailable", example = "100")
    private int addingQuantity;
}