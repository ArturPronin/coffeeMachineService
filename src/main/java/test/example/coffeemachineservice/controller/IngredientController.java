package test.example.coffeemachineservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENTS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_SUCCESS_ADD_MESSAGE;

@Tag(name = "Контроллер для взаимодействия с ингредиентами", description = "Ingredient API version v1")
public interface IngredientController {

    @Operation(
            summary = "Добавление нового ингредиента",
            description = "Эндпоинт добавления нового ингредиента"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = INGREDIENT_SUCCESS_ADD_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Существующий ингредиент",
                    content = @Content(schema = @Schema(example = INGREDIENT_ALREADY_EXISTS_MESSAGE))),
    })
    ResponseEntity<String> addIngredient(@Valid @RequestBody AddNewIngredientRequestDto requestDto);

    @Operation(
            summary = "Просмотр всех ингредиентов",
            description = "Эндпоинт просмотра всех ингредиентов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = INGREDIENTS_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<IngredientResponseDto>> getAllIngredients();

    @Operation(
            summary = "Обновление количества ингредиентов",
            description = "Напишите в addingQuantity то количество товара, которое хотите добавить к текущему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = INGREDIENT_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<IngredientResponseDto> updateAmountAvailableIngredient(UpdateIngredientRequestDto requestDto);

    @Operation(
            summary = "Удаление ингредиента",
            description = "Эндпоинт удаления ингредиента"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = INGREDIENT_DELETED_MESSAGE))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = INGREDIENT_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<String> deleteIngredient(@PathVariable
                                            @Schema(description = "ingredientId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
                                            String ingredientId);
}