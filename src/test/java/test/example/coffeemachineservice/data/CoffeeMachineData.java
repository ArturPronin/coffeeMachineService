package test.example.coffeemachineservice.data;

import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.PeriodRequestDto;
import test.example.coffeemachineservice.dto.request.RecipeIngredientDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static test.example.coffeemachineservice.persistent.enums.OrderStatus.COMPLETED;

public class CoffeeMachineData {

    public static final String RECIPE_ID = "123e4567-e89b-12d3-a456-426614174000";

    public static final String TEST_UUID = "123e4567-e89b-12d3-a456-426614174001";

    public static final String COFFEE_RAF_NAME = "Раф";

    public static final String COFFEE_MILK_NAME = "Молоко";

    public static final String TEST_INGREDIENT_UNIT = "мл";

    public static RecipeIngredientDto createRecipeIngredientDto() {
        return RecipeIngredientDto.builder()
                .ingredientName(COFFEE_MILK_NAME)
                .quantityOnRecipe(100)
                .build();
    }

    public static List<RecipeIngredientDto> createListRecipeIngredientDto(RecipeIngredientDto recipeIngredientDto) {
        List<RecipeIngredientDto> listRecipeIngredientDto = new ArrayList<>();
        listRecipeIngredientDto.add(recipeIngredientDto);
        return listRecipeIngredientDto;
    }

    public static RecipeResponseDto createRecipeResponseDto() {
        return RecipeResponseDto.builder()
                .recipeId(RECIPE_ID)
                .recipeName(COFFEE_RAF_NAME)
                .recipeIngredient(createListRecipeIngredientDto(createRecipeIngredientDto()))
                .build();
    }

    public static List<RecipeResponseDto> createListRecipeResponseDto(RecipeResponseDto recipeResponseDto) {
        List<RecipeResponseDto> listRecipeResponseDto = new ArrayList<>();
        listRecipeResponseDto.add(recipeResponseDto);
        return listRecipeResponseDto;
    }

    public static DrinkResponseDto createDrinkResponseDto() {
        return DrinkResponseDto.builder()
                .drinkId(TEST_UUID)
                .drinkName(COFFEE_RAF_NAME)
                .recipe(createRecipeResponseDto())
                .build();
    }

    public static List<DrinkResponseDto> createListDrinkResponseDto(DrinkResponseDto responseDto) {
        List<DrinkResponseDto> listDrinkResponseDto = new ArrayList<>();
        listDrinkResponseDto.add(responseDto);
        return listDrinkResponseDto;
    }

    public static AddNewDrinkRequestDto createAddNewDrinkRequestDto() {
        return AddNewDrinkRequestDto.builder()
                .recipeName(COFFEE_RAF_NAME)
                .drinkName(COFFEE_RAF_NAME)
                .build();
    }

    public static MakeDrinkRequestDto createMakeDrinkRequestDto() {
        return MakeDrinkRequestDto.builder()
                .drinkName(COFFEE_RAF_NAME)
                .build();
    }

    public static AddNewIngredientRequestDto createAddNewIngredientRequestDto() {
        return AddNewIngredientRequestDto.builder()
                .ingredientName(COFFEE_MILK_NAME)
                .amountAvailable(100)
                .unit(TEST_INGREDIENT_UNIT)
                .build();
    }

    public static IngredientResponseDto createIngredientResponseDto() {
        return IngredientResponseDto.builder()
                .ingredientId(TEST_UUID)
                .ingredientName(COFFEE_MILK_NAME)
                .amountAvailable(100)
                .unit(TEST_INGREDIENT_UNIT)
                .build();
    }

    public static List<IngredientResponseDto> createListIngredientResponseDto(IngredientResponseDto responseDto) {
        List<IngredientResponseDto> listIngredientResponseDto = new ArrayList<>();
        listIngredientResponseDto.add(responseDto);
        return listIngredientResponseDto;
    }

    public static UpdateIngredientRequestDto createUpdateIngredientRequestDto() {
        return UpdateIngredientRequestDto.builder()
                .ingredientName(COFFEE_MILK_NAME)
                .addingQuantity(100)
                .build();
    }

    public static AddNewRecipeRequestDto createAddNewRecipeRequestDto() {
        return AddNewRecipeRequestDto.builder()
                .recipeName(COFFEE_RAF_NAME)
                .recipeIngredients(createListRecipeIngredientDto(createRecipeIngredientDto()))
                .build();
    }

    public static OrderResponseDto createOrderResponseDto() {
        return OrderResponseDto.builder()
                .orderId(TEST_UUID)
                .drinkName(COFFEE_RAF_NAME)
                .createdAt(LocalDateTime.now())
                .status(COMPLETED.getStatusName())
                .build();
    }

    public static List<OrderResponseDto> createListOrderResponseDto(OrderResponseDto responseDto) {
        List<OrderResponseDto> listOrderResponseDto = new ArrayList<>();
        listOrderResponseDto.add(responseDto);
        return listOrderResponseDto;
    }

    public static PeriodRequestDto createPeriodRequestDto() {
        return PeriodRequestDto.builder()
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 4, 1))
                .build();
    }
}