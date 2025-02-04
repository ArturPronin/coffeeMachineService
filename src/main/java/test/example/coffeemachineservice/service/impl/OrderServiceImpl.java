package test.example.coffeemachineservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.exception.OrderException;
import test.example.coffeemachineservice.mapper.OrderMapper;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Order;
import test.example.coffeemachineservice.persistent.enums.OrderStatus;
import test.example.coffeemachineservice.persistent.repository.OrderRepository;
import test.example.coffeemachineservice.service.OrderService;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static test.example.coffeemachineservice.constant.ApplicationConstant.CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.ORDERS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.ORDER_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.CREATED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.PROGRESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    public UUID createOrder(Drink drink, String status) {
        boolean hasActiveOrder = orderRepository.existsByStatusIn(Set.of(CREATED.getStatusName(), PROGRESS.getStatusName()));
        if (hasActiveOrder) {
            throw new OrderException(CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE);
        }
        Order newOrder = Order.builder()
                .drink(drink)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(newOrder);
        return newOrder.getOrderId();
    }

    @Override
    public void updateOrderStatus(UUID orderId, String status) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND_MESSAGE));
        if (PROGRESS.getStatusName().equals(status) &&
                orderRepository.existsByStatusIn(Set.of(PROGRESS.getStatusName()))) {
            throw new OrderException(CAN_ONLY_ONE_ACTIVE_ORDER_MESSAGE);
        }
        foundOrder.setStatus(status);
        orderRepository.save(foundOrder);
    }

    @Override
    public void updateOrderStatusAfterDelay(UUID orderId, OrderStatus newStatus, Duration delay) {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                updateOrderStatus(orderId, newStatus.getStatusName());
                log.info("Статус заказа {} обновлен на '{}'", orderId, newStatus.getStatusName());
            } catch (OrderException e) {
                log.error("Ошибка обновления статуса заказа {}: {}", orderId, e.getMessage());
            }
        }, delay.toSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> foundOrders = orderRepository.findAll();
        if (foundOrders.isEmpty()) {
            throw new OrderException(ORDERS_NOT_FOUND_MESSAGE);
        }
        return foundOrders.stream()
                .map(orderMapper::mapToOrderResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersForToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return findOrders(start, end).stream()
                .map(orderMapper::mapToOrderResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersForCurrentWeek() {
        LocalDateTime start = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return findOrders(start, end).stream()
                .map(orderMapper::mapToOrderResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersForPeriod(LocalDate start, LocalDate end) {
        return findOrders(start.atStartOfDay(), end.atTime(23, 59, 59)).stream()
                .map(orderMapper::mapToOrderResponseDto)
                .toList();
    }

    private List<Order> findOrders(LocalDateTime start, LocalDateTime end) {
        List<Order> foundOrders = orderRepository.findByCreatedAtBetween(start, end);
        if (foundOrders.isEmpty()) {
            throw new OrderException(ORDERS_NOT_FOUND_MESSAGE);
        }
        return foundOrders;
    }
}