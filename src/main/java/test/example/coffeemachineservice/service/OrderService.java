package test.example.coffeemachineservice.service;

import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.enums.OrderStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    UUID createOrder(Drink drink, String status);

    void updateOrderStatus(UUID orderId, String status);

    void updateOrderStatusAfterDelay(UUID orderId, OrderStatus newStatus, Duration delay);

    List<OrderResponseDto> getAllOrders();

    List<OrderResponseDto> getOrdersForToday();

    List<OrderResponseDto> getOrdersForCurrentWeek();

    List<OrderResponseDto> getOrdersForPeriod(LocalDate start, LocalDate end);
}