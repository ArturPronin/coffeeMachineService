package test.example.coffeemachineservice.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.example.coffeemachineservice.persistent.entity.Drink;

import java.util.Optional;
import java.util.UUID;

public interface DrinkRepository extends JpaRepository<Drink, UUID> {

    Optional<Drink> findByDrinkName(String drinkName);

    @Query("SELECT d FROM Drink d WHERE d.ordersCount > 0 ORDER BY d.ordersCount DESC LIMIT 1")
    Optional<Drink> findMostPopularDrink();
}