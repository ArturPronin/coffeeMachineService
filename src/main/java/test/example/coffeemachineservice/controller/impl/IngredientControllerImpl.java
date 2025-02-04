package test.example.coffeemachineservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.example.coffeemachineservice.controller.IngredientController;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.request.UpdateIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.service.IngredientService;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INGREDIENT_SUCCESS_ADD_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coffee/ingredient")
public class IngredientControllerImpl implements IngredientController {

    private final IngredientService ingredientService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<String> addIngredient(@Valid @RequestBody AddNewIngredientRequestDto requestDto) {
        ingredientService.addIngredient(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(INGREDIENT_SUCCESS_ADD_MESSAGE);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @Override
    @PatchMapping("/updateAmountAvailable")
    public ResponseEntity<IngredientResponseDto> updateAmountAvailableIngredient(UpdateIngredientRequestDto requestDto) {
        return ResponseEntity.ok(ingredientService.updateAmountAvailableIngredient(requestDto));
    }

    @Override
    @DeleteMapping("/delete/{ingredientId}")
    public ResponseEntity<String> deleteIngredient(@PathVariable String ingredientId) {
        return ResponseEntity.ok(ingredientService.deleteIngredient(ingredientId));
    }
}