package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Сущность для приготовления напитка")
public class MakeDrinkRequestDto {

    @Schema(description = "drinkName", example = "Раф")
    @NotEmpty(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private String drinkName;
}