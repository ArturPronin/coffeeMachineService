package test.example.coffeemachineservice.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.persistent.entity.Ingredient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_MILK_NAME;
import static test.example.coffeemachineservice.data.CoffeeMachineData.TEST_INGREDIENT_UNIT;

@SpringBootTest
class IngredientMapperTest {

    @Autowired
    private IngredientMapper ingredientMapper;

    @Test
    void givenIngredientEntity_whenMapToIngredientResponseDto_thenSuccess() {
        Ingredient ingredient = Ingredient.builder()
                .ingredientId(UUID.randomUUID())
                .ingredientName(COFFEE_MILK_NAME)
                .amountAvailable(0)
                .unit(TEST_INGREDIENT_UNIT)
                .build();


        IngredientResponseDto responseDto = ingredientMapper.mapToIngredientResponseDto(ingredient);

        assertNotNull(responseDto);
        assertEquals(ingredient.getIngredientId(), UUID.fromString(responseDto.getIngredientId()));
        assertEquals(ingredient.getIngredientName(), responseDto.getIngredientName());
        assertEquals(ingredient.getAmountAvailable(), responseDto.getAmountAvailable());
        assertEquals(ingredient.getUnit(), responseDto.getUnit());
    }

    @Test
    void givenNullIngredientEntity_whenMapToIngredientResponseDto_thenReturnsNull() {
        Ingredient ingredient = null;
        IngredientResponseDto responseDto = ingredientMapper.mapToIngredientResponseDto(ingredient);
        assertNull(responseDto);
    }

    @Test
    void givenAddNewIngredientRequestDto_whenMapToIngredient_thenSuccess() {
        AddNewIngredientRequestDto requestDto = AddNewIngredientRequestDto.builder()
                .ingredientName(COFFEE_MILK_NAME)
                .amountAvailable(0)
                .unit(TEST_INGREDIENT_UNIT)
                .build();

        Ingredient ingredient = ingredientMapper.mapToIngredient(requestDto);

        assertNotNull(ingredient);
        assertEquals(requestDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(requestDto.getAmountAvailable(), ingredient.getAmountAvailable());
        assertEquals(requestDto.getUnit(), ingredient.getUnit());
    }

    @Test
    void givenNullAddNewIngredientRequestDto_whenMapToIngredient_thenReturnsNull() {
        AddNewIngredientRequestDto requestDto = null;
        Ingredient ingredient = ingredientMapper.mapToIngredient(requestDto);
        assertNull(ingredient);
    }
}