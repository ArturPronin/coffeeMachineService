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
import test.example.coffeemachineservice.controller.RecipeController;
import test.example.coffeemachineservice.dto.request.AddNewRecipeRequestDto;
import test.example.coffeemachineservice.dto.response.RecipeResponseDto;
import test.example.coffeemachineservice.service.RecipeService;

import java.util.List;

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCORRECT_UUID_FORMAT_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.RECIPE_SUCCESS_ADD_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.REGEXP_UUID;

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

    @Override
    @DeleteMapping("/delete/{recipeId}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable("recipeId")
            @Schema(description = "recipeId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
            @Valid @Pattern(regexp = REGEXP_UUID, message = INCORRECT_UUID_FORMAT_MESSAGE) String recipeId) {
        return ResponseEntity.ok(recipeService.deleteRecipe(recipeId));
    }
}