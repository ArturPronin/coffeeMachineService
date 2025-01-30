package test.example.coffeemachineservice.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ingredient_id", nullable = false)
    private UUID ingredientId;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    private String ingredientName;

    @Column(name = "amount_available", nullable = false)
    private int amountAvailable;

    @Column(name = "unit", nullable = false)
    private String unit;
}
