package test.example.coffeemachineservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.example.coffeemachineservice.dto.request.AddNewIngredientRequestDto;
import test.example.coffeemachineservice.dto.response.IngredientResponseDto;
import test.example.coffeemachineservice.persistent.entity.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(target = "ingredientId", ignore = true)
    Ingredient mapToIngredient(AddNewIngredientRequestDto requestDto);

    IngredientResponseDto mapToIngredientResponseDto(Ingredient ingredient);
}