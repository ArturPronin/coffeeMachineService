package test.example.coffeemachineservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность отображения заказа")
public class OrderResponseDto {

    @Schema(description = "orderId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
    private String orderId;

    @Schema(description = "drinkName", example = "Раф")
    private String drinkName;

    @Schema(description = "status", example = "В обработке")
    private String status;

    @Schema(description = "createdAt", example = "01.01.2025 12:00:00")
    private LocalDateTime createdAt;
}