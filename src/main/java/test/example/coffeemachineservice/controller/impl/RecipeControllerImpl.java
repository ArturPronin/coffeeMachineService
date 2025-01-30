package test.example.coffeemachineservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.example.coffeemachineservice.controller.RecipeController;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.service.RecipeService;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_SUCCESS_ADD_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coffee/recipe")
public class RecipeControllerImpl implements RecipeController {

    private final RecipeService recipeService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody AddNewRecipeRequestDto requestDto) {
        recipeService.addRecipe(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(RECIPE_SUCCESS_ADD_MESSAGE);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<RecipeResponseDto>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }
}
