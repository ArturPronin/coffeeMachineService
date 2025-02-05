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
@Schema(description = "Сущность отображения напитка")
public class DrinkResponseDto {

    @Schema(description = "drinkId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
    private String drinkId;

    @Schema(description = "drinkName", example = "Раф")
    private String drinkName;

    @Schema(description = "recipe", implementation = RecipeResponseDto.class)
    private RecipeResponseDto recipe;

    @Schema(description = "ordersCount", example = "5")
    private int ordersCount;
}