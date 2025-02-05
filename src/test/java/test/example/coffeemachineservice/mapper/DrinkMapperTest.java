package test.example.coffeemachineservice.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Recipe;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_RAF_NAME;

@SpringBootTest
class DrinkMapperTest {

    @Autowired
    private DrinkMapper drinkMapper;

    @Test
    void givenDrinkEntity_whenMapToDrinkResponseDto_thenSuccess() {
        Drink drink = Drink.builder()
                .drinkId(UUID.randomUUID())
                .drinkName(COFFEE_RAF_NAME)
                .recipe(Recipe.builder()
                        .recipeId(UUID.randomUUID())
                        .build())
                .build();


        DrinkResponseDto responseDto = drinkMapper.mapToDrinkResponseDto(drink);

        assertNotNull(responseDto);
        assertEquals(drink.getDrinkId(), UUID.fromString(responseDto.getDrinkId()));
        assertEquals(drink.getDrinkName(), responseDto.getDrinkName());
        assertEquals(drink.getRecipe().getRecipeId(), UUID.fromString(responseDto.getRecipe().getRecipeId()));
    }

    @Test
    void givenNullDrinkEntity_whenMapToDrinkResponseDto_thenReturnsNull() {
        Drink drink = null;
        DrinkResponseDto responseDto = drinkMapper.mapToDrinkResponseDto(drink);
        assertNull(responseDto);
    }

    @Test
    void givenAddNewDrinkRequestDto_whenMapToDrink_thenSuccess() {
        AddNewDrinkRequestDto requestDto = AddNewDrinkRequestDto.builder()
                .drinkName(COFFEE_RAF_NAME)
                .recipeName(COFFEE_RAF_NAME)
                .build();

        Drink drink = drinkMapper.mapToDrink(requestDto);

        assertNotNull(drink);
        assertEquals(requestDto.getDrinkName(), drink.getDrinkName());
    }

    @Test
    void givenNullAddNewDrinkRequestDto_whenMapToDrink_thenReturnsNull() {
        AddNewDrinkRequestDto requestDto = null;
        Drink drink = drinkMapper.mapToDrink(requestDto);
        assertNull(drink);
    }
}