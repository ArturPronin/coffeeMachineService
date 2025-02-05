package test.example.coffeemachineservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность запроса статистики за период")
public class PeriodRequestDto {

    @Schema(description = "startDate", example = "2025-01-01")
    @NotNull(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private LocalDate startDate;

    @Schema(description = "endDate", example = "2025-04-04")
    @NotNull(message = INCOMING_PARAMETER_MISSING_MESSAGE)
    private LocalDate endDate;
}