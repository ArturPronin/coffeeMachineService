package test.example.coffeemachineservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import test.example.coffeemachineservice.controller.impl.RecipeControllerImpl;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.exception.IngredientException;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPES_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewRecipeRequestDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createListRecipeResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createRecipeResponseDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeControllerImpl.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v1/coffee/recipe";

    @ParameterizedTest
    @CsvSource({
            "'Раф', 'Сливки', 1, 201, 'Рецепт добавлен'",
            "'', 'Сливки', 1, 400, 'Обязательный входной параметр отсутствует'",
            "'Раф', 'Сливки', -1, 400, 'Число должно быть больше или равно 0'",
            "'Раф', '', 1, 400, 'Обязательный входной параметр отсутствует'"
    })
    void validOrInvalidIncomingParameters_whenAddRecipe(
            String recipeName, String ingredientName, int quantityOnRecipe, int expectedStatus, String content) throws Exception {
        RecipeIngredientDto recipeIngredientDto = RecipeIngredientDto.builder()
                .ingredientName(ingredientName)
                .quantityOnRecipe(quantityOnRecipe)
                .build();

        List<RecipeIngredientDto> listRecipeIngredientDto = new ArrayList<>();
        listRecipeIngredientDto.add(recipeIngredientDto);

        AddNewRecipeRequestDto requestDto = AddNewRecipeRequestDto.builder()
                .recipeName(recipeName)
                .recipeIngredients(listRecipeIngredientDto)
                .build();

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(recipeService, expectedStatus == 400 ? never() : times(1)).addRecipe(any());
    }

    @ParameterizedTest
    @CsvSource({
            "400, 'Рецепт с указанным названием уже существует'",
            "404, 'Ингредиент с указанным названием не найден'"
    })
    void givenExistingRecipeOrNonExistingIngredient_whenAddRecipe_thenReturnBadRequest(
            int expectedStatus, String content) throws Exception {
        AddNewRecipeRequestDto requestDto = createAddNewRecipeRequestDto();

        switch (expectedStatus) {
            case 400:
                doThrow(new RecipeException(content)).when(recipeService).addRecipe(any());
                break;
            case 404:
                doThrow(new IngredientException(NOT_FOUND, content)).when(recipeService).addRecipe(any());
                break;
        }

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(recipeService, times(1)).addRecipe(any());
    }

    @Test
    void testGetAllRecipes_thenReturnResponseDto() throws Exception {
        List<RecipeResponseDto> listRecipeResponseDto = createListRecipeResponseDto(createRecipeResponseDto());
        when(recipeService.getAllRecipes()).thenReturn(listRecipeResponseDto);

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listRecipeResponseDto)));

        verify(recipeService, times(1)).getAllRecipes();
    }

    @Test
    void givenNonExistingRecipe_whenGetAllRecipes_thenReturnNotFound() throws Exception {
        doThrow(new RecipeException(NOT_FOUND, RECIPES_NOT_FOUND_MESSAGE)).when(recipeService).getAllRecipes();

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(RECIPES_NOT_FOUND_MESSAGE));

        verify(recipeService, times(1)).getAllRecipes();
    }

    @ParameterizedTest
    @CsvSource({
            "'123e4567-e89b-12d3-a456-426614174001', 200, 'Рецепт удален'",
            "'123e4567-e89b-12d3-a456-42661417', 400, 'Неверный формат UUID'",
            "'123e4567-e89b-12d3-a456-426614174001', 404, 'Рецепт с указанным ID не найден'"
    })
    void parametrizedTest_whenDeleteRecipe(
            String recipeId, int expectedStatus, String content) throws Exception {
        switch (expectedStatus) {
            case 200:
                when(recipeService.deleteRecipe(recipeId)).thenReturn(content);
                break;
            case 404:
                doThrow(new RecipeException(NOT_FOUND, content)).when(recipeService).deleteRecipe(any());
                break;
        }

        mockMvc.perform(delete(baseUrl + "/delete/{recipeId}", recipeId))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(recipeService, expectedStatus == 400 ? never() : times(1))
                .deleteRecipe(recipeId);
    }
}