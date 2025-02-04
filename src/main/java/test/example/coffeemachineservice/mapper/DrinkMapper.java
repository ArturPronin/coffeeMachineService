package test.example.coffeemachineservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.persistent.entity.Drink;

@Mapper(componentModel = "spring", uses = {RecipeMapper.class})
public interface DrinkMapper {

    @Mapping(target = "drinkId", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    Drink mapToDrink(AddNewDrinkRequestDto requestDto);

    @Mapping(source = "drinkId", target = "drinkId")
    @Mapping(source = "drinkName", target = "drinkName")
    @Mapping(source = "recipe", target = "recipe")
    @Mapping(source = "ordersCount", target = "ordersCount")
    DrinkResponseDto mapToDrinkResponseDto(Drink drink);
}