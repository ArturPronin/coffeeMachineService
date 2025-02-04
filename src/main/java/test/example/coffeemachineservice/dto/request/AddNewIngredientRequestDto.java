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
@Schema(description = "Сущность добавления нового ингредиента")
public class AddNewIngredientRequestDto {

    @Schema(description = "Название", example = "Сливки")
    private String ingredientName;

    @Schema(description = "Доступное количество", example = "100")
    private int amountAvailable;

    @Schema(description = "Мерные единицы", example = "мл")
    private String unit;
}