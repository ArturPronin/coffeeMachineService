package test.example.coffeemachineservice.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.example.coffeemachineservice.persistent.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsByStatusIn(Set<String> statuses);

    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}