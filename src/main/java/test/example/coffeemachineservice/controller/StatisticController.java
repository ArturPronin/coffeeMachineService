package test.example.coffeemachineservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import test.example.coffeemachineservice.dto.request.PeriodRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.ORDERS_NOT_FOUND_MESSAGE;

@Tag(name = "Контроллер для взаимодействия со статистикой", description = "Statistic API version v1")
public interface StatisticController {

    @Operation(
            summary = "Просмотр самого популярного напитка",
            description = "Эндпоинт просмотра самого популярного напитка"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = ORDERS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<DrinkResponseDto> getPopularDrink();

    @Operation(
            summary = "Просмотр всех заказов",
            description = "Эндпоинт для просмотра всех заказов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = ORDERS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<OrderResponseDto>> getAllOrders();

    @Operation(
            summary = "Просмотр заказов за текущий день",
            description = "Эндпоинт для просмотра заказов за текущий день"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = ORDERS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<OrderResponseDto>> getOrdersForToday();

    @Operation(
            summary = "Просмотр заказов за текущую неделю",
            description = "Эндпоинт для просмотра заказов текущую за неделю"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = ORDERS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<OrderResponseDto>> getOrdersForCurrentWeek();

    @Operation(
            summary = "Просмотр заказов за указанный период",
            description = "Эндпоинт для просмотра заказов за указанный период"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = ORDERS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<OrderResponseDto>> getOrdersForPeriod(@Valid @RequestBody PeriodRequestDto requestDto);
}