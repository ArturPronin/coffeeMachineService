package test.example.coffeemachineservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.exception.DrinkException;
import test.example.coffeemachineservice.exception.RecipeException;
import test.example.coffeemachineservice.mapper.DrinkMapper;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Recipe;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;
import test.example.coffeemachineservice.persistent.repository.DrinkRepository;
import test.example.coffeemachineservice.persistent.repository.RecipeRepository;
import test.example.coffeemachineservice.service.DrinkService;
import test.example.coffeemachineservice.service.IngredientService;
import test.example.coffeemachineservice.service.OrderService;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINKS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_ALREADY_EXISTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_DELETED_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_ID_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.MINUTES_MAKE_DRINK;
import static test.example.coffeemachineservice.constant.ApplicationConstant.NOT_ENOUGH_INGREDIENTS_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.POPULAR_DRINK_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.WAIT_UNTIL_READY_MESSAGE;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.COMPLETED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.CREATED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.PROGRESS;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.REFUSED;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrinkServiceImpl implements DrinkService {

    private final DrinkRepository drinkRepository;

    private final DrinkMapper drinkMapper;

    private final RecipeRepository recipeRepository;

    private final OrderService orderService;

    private final IngredientService ingredientService;

    @Override
    public void addDrink(AddNewDrinkRequestDto requestDto) {
        if (drinkRepository.findByDrinkName(requestDto.getDrinkName()).isPresent()) {
            throw new DrinkException(DRINK_ALREADY_EXISTS_MESSAGE);
        }
        Drink newDrink = drinkMapper.mapToDrink(requestDto);
        Recipe foundRecipe = recipeRepository.findByRecipeName(requestDto.getRecipeName())
                .orElseThrow(() -> new RecipeException(RECIPE_NOT_FOUND_MESSAGE));
        newDrink.setRecipe(foundRecipe);
        drinkRepository.save(newDrink);
    }

    @Override
    public List<DrinkResponseDto> getAllDrinks() {
        List<Drink> foundDrinks = drinkRepository.findAll();
        if (foundDrinks.isEmpty()) {
            throw new DrinkException(DRINKS_NOT_FOUND_MESSAGE);
        }
        return foundDrinks.stream()
                .map(drinkMapper::mapToDrinkResponseDto)
                .toList();
    }

    @Override
    public String makeDrink(MakeDrinkRequestDto requestDto) {
        Drink foundDrink = drinkRepository.findByDrinkName(requestDto.getDrinkName())
                .orElseThrow(() -> new DrinkException(NOT_FOUND, DRINK_NOT_FOUND_MESSAGE));
        List<RecipeIngredient> recipeIngredients = foundDrink.getRecipe().getRecipeIngredients();
        UUID orderId = orderService.createOrder(foundDrink, CREATED.getStatusName());
        if (!ingredientService.checkAmountIngredients(recipeIngredients)) {
            orderService.updateOrderStatus(orderId, REFUSED.getStatusName());
            throw new DrinkException(NOT_ENOUGH_INGREDIENTS_MESSAGE);
        }
        ingredientService.reduceIngredientQuantities(recipeIngredients);
        orderService.updateOrderStatus(orderId, PROGRESS.getStatusName());
        orderService.updateOrderStatusAfterDelay(orderId, COMPLETED, Duration.ofMinutes(MINUTES_MAKE_DRINK));
        foundDrink.incrementOrdersCount();
        drinkRepository.save(foundDrink);
        return WAIT_UNTIL_READY_MESSAGE;
    }

    @Override
    public DrinkResponseDto getPopularDrink() {
        Drink popularDrink = drinkRepository.findMostPopularDrink()
                .orElseThrow(() -> new DrinkException(NOT_FOUND, POPULAR_DRINK_NOT_FOUND_MESSAGE));
        return drinkMapper.mapToDrinkResponseDto(popularDrink);
    }

    @Override
    public String deleteDrink(String drinkId) {
        Drink foundDrink = drinkRepository.findById(UUID.fromString(drinkId))
                .orElseThrow(() -> new DrinkException(DRINK_ID_NOT_FOUND_MESSAGE));
        drinkRepository.delete(foundDrink);
        return DRINK_DELETED_MESSAGE;
    }
}