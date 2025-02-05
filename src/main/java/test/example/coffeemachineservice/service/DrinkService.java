package test.example.coffeemachineservice.service;

import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;

import java.util.List;

public interface DrinkService {

    void addDrink(AddNewDrinkRequestDto requestDto);

    List<DrinkResponseDto> getAllDrinks();

    String makeDrink(MakeDrinkRequestDto requestDto);

    DrinkResponseDto getPopularDrink();

    String deleteDrink(String drinkId);
}