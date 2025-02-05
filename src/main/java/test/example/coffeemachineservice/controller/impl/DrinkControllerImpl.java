package test.example.coffeemachineservice.controller.impl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.example.coffeemachineservice.controller.DrinkController;
import test.example.coffeemachineservice.dto.request.AddNewDrinkRequestDto;
import test.example.coffeemachineservice.dto.request.MakeDrinkRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.service.DrinkService;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.DRINK_SUCCESS_ADD_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INCORRECT_UUID_FORMAT_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.REGEXP_UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coffee/drink")
public class DrinkControllerImpl implements DrinkController {

    private final DrinkService drinkService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<String> addDrink(@Valid @RequestBody AddNewDrinkRequestDto requestDto) {
        drinkService.addDrink(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DRINK_SUCCESS_ADD_MESSAGE);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<DrinkResponseDto>> getAllDrinks() {
        return ResponseEntity.ok(drinkService.getAllDrinks());
    }

    @Override
    @PostMapping("/makeCoffee")
    public ResponseEntity<String> makeDrink(@Valid @RequestBody MakeDrinkRequestDto requestDto) {
        return ResponseEntity.ok(drinkService.makeDrink(requestDto));
    }

    @Override
    @DeleteMapping("/delete/{drinkId}")
    public ResponseEntity<String> deleteDrink(
            @PathVariable("drinkId")
            @Schema(description = "drinkId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
            @Valid @Pattern(regexp = REGEXP_UUID, message = INCORRECT_UUID_FORMAT_MESSAGE) String drinkId) {
        return ResponseEntity.ok(drinkService.deleteDrink(drinkId));
    }
}