package test.example.coffeemachineservice.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.example.coffeemachineservice.persistent.entity.Ingredient;

import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    Optional<Ingredient> findByIngredientName(String ingredientName);
}
