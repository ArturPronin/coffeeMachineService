package test.example.coffeemachineservice.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.example.coffeemachineservice.persistent.entity.RecipeIngredient;

import java.util.UUID;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, UUID> {
}
