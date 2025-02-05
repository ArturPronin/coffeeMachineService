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
import test.example.coffeemachineservice.controller.impl.IngredientControllerImpl;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.exception.IngredientException;
import test.example.coffeemachineservice.service.IngredientService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENTS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewIngredientRequestDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createIngredientResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createListIngredientResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createUpdateIngredientRequestDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IngredientControllerImpl.class)
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v1/coffee/ingredient";

    @ParameterizedTest
    @CsvSource({
            "'Сливки', 100, 'мл', 201, 'Ингредиент добавлен'",
            "'', 100, 'мл', 400, 'Обязательный входной параметр отсутствует'",
            "'Сливки', -1, 'мл', 400, 'Число должно быть больше или равно 0'",
            "'Сливки', 100, '', 400, 'Обязательный входной параметр отсутствует'"
    })
    void validOrInvalidIncomingParameters_whenAddIngredient(
            String ingredientName, int amountAvailable, String unit, int expectedStatus, String content) throws Exception {
        AddNewIngredientRequestDto requestDto = AddNewIngredientRequestDto.builder()
                .ingredientName(ingredientName)
                .amountAvailable(amountAvailable)
                .unit(unit)
                .build();

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(ingredientService, expectedStatus == 400 ? never() : times(1)).addIngredient(any());
    }

    @Test
    void givenExistingIngredient_whenAddIngredient_thenReturnBadRequest() throws Exception {
        AddNewIngredientRequestDto requestDto = createAddNewIngredientRequestDto();

        doThrow(new IngredientException(INGREDIENT_ALREADY_EXISTS_MESSAGE)).when(ingredientService).addIngredient(any());

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INGREDIENT_ALREADY_EXISTS_MESSAGE));

        verify(ingredientService, times(1)).addIngredient(any());
    }

    @Test
    void testGetAllIngredients_thenReturnResponseDto() throws Exception {
        List<IngredientResponseDto> listIngredientResponseDto = createListIngredientResponseDto(createIngredientResponseDto());
        when(ingredientService.getAllIngredients()).thenReturn(listIngredientResponseDto);

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listIngredientResponseDto)));

        verify(ingredientService, times(1)).getAllIngredients();
    }

    @Test
    void givenNonExistingIngredients_whenGetAllIngredients_thenReturnNotFound() throws Exception {
        doThrow(new IngredientException(NOT_FOUND, INGREDIENTS_NOT_FOUND_MESSAGE)).when(ingredientService).getAllIngredients();

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(INGREDIENTS_NOT_FOUND_MESSAGE));

        verify(ingredientService, times(1)).getAllIngredients();
    }

    @ParameterizedTest
    @CsvSource({
            "'123e4567-e89b-12d3-a456-426614174001', 200, 'Ингредиент удален'",
            "'123e4567-e89b-12d3-a456-42661417', 400, 'Неверный формат UUID'",
            "'123e4567-e89b-12d3-a456-426614174001', 404, 'Ингредиент с указанным ID не найден'"
    })
    void parametrizedTest_whenDeleteIngredient(
            String ingredientId, int expectedStatus, String content) throws Exception {
        switch (expectedStatus) {
            case 200:
                when(ingredientService.deleteIngredient(ingredientId)).thenReturn(content);
                break;
            case 404:
                doThrow(new IngredientException(NOT_FOUND, content)).when(ingredientService).deleteIngredient(any());
                break;
        }

        mockMvc.perform(delete(baseUrl + "/delete/{ingredientId}", ingredientId))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(ingredientService, expectedStatus == 400 ? never() : times(1))
                .deleteIngredient(ingredientId);
    }

    @Test
    void givenValidRequestDto_whenUpdateAmountAvailableIngredient_thenReturnResponseDto() throws Exception {
        UpdateIngredientRequestDto requestDto = createUpdateIngredientRequestDto();
        IngredientResponseDto responseDto = createIngredientResponseDto();
        when(ingredientService.updateAmountAvailableIngredient(any(UpdateIngredientRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch(baseUrl + "/updateAmountAvailable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(ingredientService, times(1))
                .updateAmountAvailableIngredient(any(UpdateIngredientRequestDto.class));

    }

    @ParameterizedTest
    @CsvSource({
            "'', 100, 400, 'Обязательный входной параметр отсутствует'",
            "'Молоко', 0, 400, 'Число должно быть больше 0'",
            "'Сливки', 100, 404, 'Ингредиент с указанным названием не найден'"
    })
    void parametrizedTest_whenUpdateAmountAvailableIngredient(
            String ingredientName, int addingQuantity, int expectedStatus, String content) throws Exception {
        UpdateIngredientRequestDto requestDto = UpdateIngredientRequestDto.builder()
                .ingredientName(ingredientName)
                .addingQuantity(addingQuantity)
                .build();
        IngredientResponseDto responseDto = createIngredientResponseDto();
        switch (expectedStatus) {
            case 200:
                when(ingredientService.updateAmountAvailableIngredient(requestDto)).thenReturn(responseDto);
                break;
            case 404:
                doThrow(new IngredientException(NOT_FOUND, content)).when(ingredientService).updateAmountAvailableIngredient(any());
                break;
        }

        mockMvc.perform(patch(baseUrl + "/updateAmountAvailable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(ingredientService, expectedStatus == 400 ? never() : times(1))
                .updateAmountAvailableIngredient(any(UpdateIngredientRequestDto.class));
    }
}