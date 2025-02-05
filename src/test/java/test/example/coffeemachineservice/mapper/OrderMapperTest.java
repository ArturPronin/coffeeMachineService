package test.example.coffeemachineservice.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static test.example.coffeemachineservice.data.CoffeeMachineData.COFFEE_RAF_NAME;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.COMPLETED;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void givenOrderEntity_whenMapToOrderResponseDto_thenSuccess() {
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .drink(Drink.builder()
                        .drinkName(COFFEE_RAF_NAME)
                        .build())
                .status(COMPLETED.getStatusName())
                .createdAt(LocalDateTime.now())
                .build();

        OrderResponseDto responseDto = orderMapper.mapToOrderResponseDto(order);

        assertNotNull(responseDto);
        assertEquals(order.getOrderId(), UUID.fromString(responseDto.getOrderId()));
        assertEquals(order.getDrink().getDrinkName(), responseDto.getDrinkName());
        assertEquals(order.getStatus(), responseDto.getStatus());
        assertEquals(order.getCreatedAt(), responseDto.getCreatedAt());
    }

    @Test
    void givenNullOrderEntity_whenMapToOrderResponseDto_thenReturnsNull() {
        Order order = null;
        OrderResponseDto responseDto = orderMapper.mapToOrderResponseDto(order);
        assertNull(responseDto);
    }
}