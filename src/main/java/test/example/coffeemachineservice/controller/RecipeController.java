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
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INCORRECT_UUID_FORMAT_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPES_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_ID_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_SUCCESS_ADD_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.REGEXP_UUID;

@Tag(name = "Контроллер для взаимодействия с рецептами", description = "Recipe API version v1")
public interface RecipeController {

    @Operation(
            summary = "Добавление нового рецепта",
            description = "Эндпоинт добавления нового рецепта"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = RECIPE_SUCCESS_ADD_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(
                            schema = @Schema(oneOf = {String.class}),
                            examples = {
                                    @ExampleObject(name = "Существующий рецепт", value = RECIPE_ALREADY_EXISTS_MESSAGE),
                                    @ExampleObject(name = "Отсутствующий параметр", value = INCOMING_PARAMETER_MISSING_MESSAGE),
                                    @ExampleObject(name = "Параметр должен быть >= 0", value = MUST_BE_GREATER_THAN_OR_EQUAL_ZERO_MESSAGE),
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Неизвестный ингредиент",
                    content = @Content(schema = @Schema(example = INGREDIENT_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<String> addRecipe(@Valid @RequestBody AddNewRecipeRequestDto requestDto);

    @Operation(
            summary = "Просмотр всех рецептов",
            description = "Эндпоинт просмотра всех рецептов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = RecipeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = RECIPES_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<List<RecipeResponseDto>> getAllRecipes();

    @Operation(
            summary = "Просмотр всех рецептов",
            description = "Эндпоинт просмотра всех рецептов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(example = RECIPE_DELETED_MESSAGE))),
            @ApiResponse(responseCode = "400", description = "Неверный UUID",
                    content = @Content(schema = @Schema(example = INCORRECT_UUID_FORMAT_MESSAGE))),
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(schema = @Schema(example = RECIPE_ID_NOT_FOUND_MESSAGE)))
    })
    ResponseEntity<String> deleteRecipe(
            @PathVariable
            @Schema(description = "recipeId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
            @Valid @Pattern(regexp = REGEXP_UUID, message = INCORRECT_UUID_FORMAT_MESSAGE) String recipeId);
}