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
import test.example.coffeemachineservice.controller.impl.DrinkControllerImpl;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.exception.DrinkException;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.service.DrinkService;

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
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINKS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_ENOUGH_INGREDIENTS_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createAddNewDrinkRequestDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createDrinkResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createListDrinkResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createMakeDrinkRequestDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DrinkControllerImpl.class)
class DrinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DrinkService drinkService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v1/coffee/drink";

    @ParameterizedTest
    @CsvSource({
            "'Раф', 'Раф', 201, 'Напиток добавлен'",
            "'', 'Раф', 400, 'Обязательный входной параметр отсутствует'",
            "'Раф', '', 400, 'Обязательный входной параметр отсутствует'"
    })
    void validOrInvalidIncomingParameters_whenAddDrink(
            String drinkName, String recipeName, int expectedStatus, String content) throws Exception {
        AddNewDrinkRequestDto requestDto = AddNewDrinkRequestDto.builder()
                .drinkName(drinkName)
                .recipeName(recipeName)
                .build();

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(drinkService, expectedStatus == 400 ? never() : times(1)).addDrink(any());
    }

    @ParameterizedTest
    @CsvSource({
            "400, 'Напиток с указанным названием уже существует'",
            "404, 'Рецепт с указанным названием не найден'"
    })
    void givenExistingDrinkOrNonExistRecipe_whenAddDrink(int expectedStatus, String content) throws Exception {
        AddNewDrinkRequestDto requestDto = createAddNewDrinkRequestDto();
        if (expectedStatus == 400) {
            doThrow(new DrinkException(content)).when(drinkService).addDrink(any());
        } else {
            doThrow(new RecipeException(NOT_FOUND, content)).when(drinkService).addDrink(any());
        }

        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(drinkService, times(1)).addDrink(any());
    }

    @Test
    void testGetAllDrinks_thenReturnResponseDto() throws Exception {
        List<DrinkResponseDto> listDrinkResponseDto = createListDrinkResponseDto(createDrinkResponseDto());
        when(drinkService.getAllDrinks()).thenReturn(listDrinkResponseDto);

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listDrinkResponseDto)));

        verify(drinkService, times(1)).getAllDrinks();
    }

    @Test
    void givenNonExistingDrink_whenGetAllDrinks_thenReturnNotFound() throws Exception {
        doThrow(new DrinkException(NOT_FOUND, DRINKS_NOT_FOUND_MESSAGE)).when(drinkService).getAllDrinks();

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(DRINKS_NOT_FOUND_MESSAGE));

        verify(drinkService, times(1)).getAllDrinks();
    }

    @ParameterizedTest
    @CsvSource({
            "'123e4567-e89b-12d3-a456-426614174001', 200, 'Напиток удален'",
            "'123e4567-e89b-12d3-a456-42661417', 400, 'Неверный формат UUID'",
            "'123e4567-e89b-12d3-a456-426614174001', 404, 'Напиток с указанным ID не найден'"
    })
    void parametrizedTest_whenDeleteDrink(
            String drinkId, int expectedStatus, String content) throws Exception {
        switch (expectedStatus) {
            case 200:
                when(drinkService.deleteDrink(drinkId)).thenReturn(content);
                break;
            case 404:
                doThrow(new DrinkException(NOT_FOUND, content)).when(drinkService).deleteDrink(any());
                break;
        }

        mockMvc.perform(delete(baseUrl + "/delete/{drinkId}", drinkId))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(drinkService, expectedStatus == 400 ? never() : times(1)).deleteDrink(drinkId);
    }

    @ParameterizedTest
    @CsvSource({
            "'Раф', 200, 'Напиток готовится, подождите 2 минуты...'",
            "'', 400, 'Обязательный входной параметр отсутствует'",
            "'Раф', 404, 'Напиток с указанным названием не найден'"
    })
    void parametrizedTest_whenMakeDrink(
            String drinkName, int expectedStatus, String content) throws Exception {
        MakeDrinkRequestDto requestDto = MakeDrinkRequestDto.builder()
                .drinkName(drinkName)
                .build();

        switch (expectedStatus) {
            case 200:
                when(drinkService.makeDrink(any())).thenReturn(content);
                break;
            case 404:
                doThrow(new DrinkException(NOT_FOUND, content)).when(drinkService).makeDrink(any());
                break;
        }

        mockMvc.perform(post(baseUrl + "/makeCoffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(content));

        verify(drinkService, expectedStatus == 400 ? never() : times(1)).makeDrink(any());
    }

    @Test
    void givenNotEnoughIngredients_whenMakeDrink_thenBadRequest() throws Exception {
        MakeDrinkRequestDto requestDto = createMakeDrinkRequestDto();

        doThrow(new DrinkException(NOT_ENOUGH_INGREDIENTS_MESSAGE)).when(drinkService).makeDrink(any());

        mockMvc.perform(post(baseUrl + "/makeCoffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(NOT_ENOUGH_INGREDIENTS_MESSAGE));

        verify(drinkService, times(1)).makeDrink(any());
    }
}