package test.example.coffeemachineservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINKS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_ID_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_SUCCESS_ADD_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INCORRECT_UUID_FORMAT_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_ENOUGH_INGREDIENTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.REGEXP_UUID;
import static test.example.coffeemachineservice.constant.ApplicationConstant.WAIT_UNTIL_READY_MESSAGE;

@Tag(name = "Контроллер для взаимодействия с напитками", description = "Drink API version v1")
public interface DrinkController {

    @Operation(
            summary = "Добавление нового напитка",
            description = "Эндпоинт добавления нового напитка"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = DRINK_SUCCESS_ADD_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(
                            schema = @Schema(oneOf = {String.class}),
                            examples = {
                                    @ExampleObject(name = "Существующий напиток", value = DRINK_ALREADY_EXISTS_MESSAGE),
                                    @ExampleObject(name = "Отсутствующий параметр", value = INCOMING_PARAMETER_MISSING_MESSAGE)
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = RECIPE_NOT_FOUND_MESSAGE))),
    })
    ResponseEntity<String> addDrink(@Valid @RequestBody AddNewDrinkRequestDto requestDto);

    @Operation(
            summary = "Просмотр всех напитков",
            description = "Эндпоинт просмотра всех напитков"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = DrinkResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = DRINKS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<DrinkResponseDto>> getAllDrinks();

    @Operation(
            summary = "Приготовить напиток",
            description = "Эндпоинт приготовления напитка"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = WAIT_UNTIL_READY_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(
                            schema = @Schema(oneOf = {String.class}),
                            examples = {
                                    @ExampleObject(name = "Один активный заказ", value = CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE),
                                    @ExampleObject(name = "Недостаточно ингредиентов", value = NOT_ENOUGH_INGREDIENTS_MESSAGE)
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = DRINK_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<String> makeDrink(@Valid @RequestBody MakeDrinkRequestDto requestDto);

    @Operation(
            summary = "Удалить напиток",
            description = "Эндпоинт удаления напитка"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = DRINK_DELETED_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Неверный UUID",
                    content = @Content(schema = @Schema(example = INCORRECT_UUID_FORMAT_MESSAGE))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = DRINK_ID_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<String> deleteDrink(
            @PathVariable("drinkId")
            @Schema(description = "drinkId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
            @Valid @Pattern(regexp = REGEXP_UUID, message = INCORRECT_UUID_FORMAT_MESSAGE) String drinkId);
}