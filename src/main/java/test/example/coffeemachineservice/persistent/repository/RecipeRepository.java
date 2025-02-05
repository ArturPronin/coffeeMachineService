package test.example.coffeemachineservice.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.example.coffeemachineservice.persistent.entity.Recipe;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    Optional<Recipe> findByRecipeName(String recipeName);
}
