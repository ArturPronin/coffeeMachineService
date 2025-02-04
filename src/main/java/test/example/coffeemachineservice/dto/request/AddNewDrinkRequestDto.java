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
@Schema(description = "Сущность добавления нового напитка")
public class AddNewDrinkRequestDto {

    @Schema(description = "Название", example = "Раф")
    private String drinkName;

    @Schema(description = "Название рецепта", example = "Раф")
    private String recipeName;
}