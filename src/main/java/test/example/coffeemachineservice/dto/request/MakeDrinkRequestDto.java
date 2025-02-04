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
@Schema(description = "Сущность для приготовления напитка")
public class MakeDrinkRequestDto {

    @Schema(description = "drinkName", example = "Раф")
    private String drinkName;
}