package test.example.coffeemachineservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import test.example.coffeemachineservice.dto.request.PeriodRequestDto;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.exception.OrderException;
import test.example.coffeemachineservice.mapper.OrderMapper;
import test.example.coffeemachineservice.persistent.entity.Drink;
import test.example.coffeemachineservice.persistent.entity.Order;
import test.example.coffeemachineservice.persistent.repository.OrderRepository;
import test.example.coffeemachineservice.service.impl.OrderServiceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static test.example.coffeemachineservice.constant.ApplicationConstant.ORDER_DELETED_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.TEST_UUID;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.COMPLETED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.CREATED;
import static test.example.coffeemachineservice.persistent.enums.OrderStatus.PROGRESS;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final UUID orderId = UUID.randomUUID();

    @Test
    void givenNoActiveOrders_whenCreateOrder_thenReturnsSuccess() {
        Drink drink = Drink.builder().build();
        String status = CREATED.getStatusName();
        Order newOrder = Order.builder()
                .drink(drink)
                .status(status)
                .createdAt(LocalDateTime.now())
                .orderId(UUID.randomUUID())
                .build();

        when(orderRepository.existsByStatusIn(Set.of(CREATED.getStatusName(), PROGRESS.getStatusName())))
                .thenReturn(false);
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(newOrder);

        UUID result = orderService.createOrder(drink, status);

        assertNotNull(result);
        verify(orderRepository).existsByStatusIn(Set.of(CREATED.getStatusName(), PROGRESS.getStatusName()));
        verify(orderRepository).save(Mockito.any(Order.class));
    }

    @Test
    void givenActiveOrderExists_whenCreateOrder_thenReturnsBadRequest() {
        Drink drink = Drink.builder().build();
        String status = CREATED.getStatusName();

        when(orderRepository.existsByStatusIn(Set.of(CREATED.getStatusName(), PROGRESS.getStatusName())))
                .thenReturn(true);

        assertThrows(OrderException.class, () -> orderService.createOrder(drink, status));
        verify(orderRepository).existsByStatusIn(Set.of(CREATED.getStatusName(), PROGRESS.getStatusName()));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void givenValidOrderIdAndStatus_whenUpdateOrderStatus_thenReturnsSuccess() {
        String status = COMPLETED.getStatusName();
        Order order = Order.builder()
                .orderId(orderId)
                .status(PROGRESS.getStatusName())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, status);

        assertEquals(status, order.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void givenNonExistingOrderId_whenUpdateOrderStatus_thenReturnsNotFound() {
        String status = COMPLETED.getStatusName();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderException.class, () -> orderService.updateOrderStatus(orderId, status));
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void givenActiveOrderExists_whenUpdateOrderStatusToProgress_thenReturnsBadRequest() {
        String status = PROGRESS.getStatusName();
        Order order = Order.builder().orderId(orderId).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.existsByStatusIn(Set.of(PROGRESS.getStatusName()))).thenReturn(true);

        assertThrows(OrderException.class, () -> orderService.updateOrderStatus(orderId, status));
        verify(orderRepository).findById(orderId);
        verify(orderRepository).existsByStatusIn(Set.of(PROGRESS.getStatusName()));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void givenExistingOrders_whenGetAllOrders_thenReturnsSuccess() {
        List<Order> orders = List.of(
                Order.builder().build(),
                Order.builder().build()
        );
        List<OrderResponseDto> expectedResponse = List.of(
                OrderResponseDto.builder().build(),
                OrderResponseDto.builder().build()
        );

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.mapToOrderResponseDto(Mockito.any())).thenReturn(OrderResponseDto.builder().build());

        List<OrderResponseDto> result = orderService.getAllOrders();

        assertEquals(expectedResponse.size(), result.size());
        verify(orderRepository).findAll();
        verify(orderMapper, times(2)).mapToOrderResponseDto(Mockito.any());
    }

    @Test
    void givenNoOrders_whenGetAllOrders_thenReturnsNotFound() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(OrderException.class, () -> orderService.getAllOrders());
        verify(orderRepository).findAll();
        verifyNoInteractions(orderMapper);
    }

    @Test
    void givenExistingOrders_whenGetOrdersForToday_thenReturnsSuccess() {
        List<Order> orders = List.of(Order.builder().build());

        when(orderRepository.findByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(orders);
        when(orderMapper.mapToOrderResponseDto(Mockito.any())).thenReturn(OrderResponseDto.builder().build());

        List<OrderResponseDto> result = orderService.getOrdersForToday();

        assertFalse(result.isEmpty());
        verify(orderRepository).findByCreatedAtBetween(Mockito.any(), Mockito.any());
        verify(orderMapper).mapToOrderResponseDto(Mockito.any());
    }

    @Test
    void givenNoOrders_whenGetOrdersForToday_thenReturnsNotFound() {

        when(orderRepository.findByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        assertThrows(OrderException.class, () -> orderService.getOrdersForToday());
        verify(orderRepository).findByCreatedAtBetween(Mockito.any(), Mockito.any());
        verifyNoInteractions(orderMapper);
    }

    @Test
    void givenExistingOrders_whenGetOrdersForCurrentWeek_thenReturnsSuccess() {
        List<Order> orders = List.of(Order.builder().build());

        when(orderRepository.findByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(orders);
        when(orderMapper.mapToOrderResponseDto(Mockito.any())).thenReturn(OrderResponseDto.builder().build());

        List<OrderResponseDto> result = orderService.getOrdersForCurrentWeek();

        assertFalse(result.isEmpty());
        verify(orderRepository).findByCreatedAtBetween(Mockito.any(), Mockito.any());
        verify(orderMapper).mapToOrderResponseDto(Mockito.any());
    }

    @Test
    void givenNoOrders_whenGetOrdersForCurrentWeek_thenReturnsNotFound() {
        when(orderRepository.findByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

        assertThrows(OrderException.class, () -> orderService.getOrdersForCurrentWeek());
        verify(orderRepository).findByCreatedAtBetween(Mockito.any(), Mockito.any());
        verifyNoInteractions(orderMapper);
    }

    @Test
    void givenValidPeriod_whenGetOrdersForPeriod_thenReturnsSuccess() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        PeriodRequestDto requestDto = PeriodRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
        List<Order> orders = List.of(Order.builder().build());

        when(orderRepository.findByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        )).thenReturn(orders);
        when(orderMapper.mapToOrderResponseDto(Mockito.any())).thenReturn(OrderResponseDto.builder().build());

        List<OrderResponseDto> result = orderService.getOrdersForPeriod(requestDto);

        assertFalse(result.isEmpty());
        verify(orderRepository).findByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
        verify(orderMapper).mapToOrderResponseDto(Mockito.any());
    }

    @Test
    void givenNoOrdersInPeriod_whenGetOrdersForPeriod_thenReturnsNotFound() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        PeriodRequestDto requestDto = PeriodRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(orderRepository.findByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        )).thenReturn(Collections.emptyList());

        assertThrows(OrderException.class, () -> orderService.getOrdersForPeriod(requestDto));
        verify(orderRepository).findByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
        verifyNoInteractions(orderMapper);
    }

    @Test
    void givenValidOrderId_whenUpdateOrderStatusAfterDelay_thenReturnsSuccess() {
        Order order = Order.builder().orderId(orderId).status(PROGRESS.getStatusName()).build();
        Duration delay = Duration.ofSeconds(1);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatusAfterDelay(orderId, COMPLETED, delay);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(orderRepository, timeout(2000)).findById(orderId);
        verify(orderRepository, timeout(2000)).save(order);
    }

    @Test
    void givenValidOrderId_whenDeleteOrder_thenReturnsSuccess() {
        Order order = Order.builder().build();
        when(orderRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.of(order));

        String result = orderService.deleteOrder(TEST_UUID);

        assertEquals(ORDER_DELETED_MESSAGE, result);
        verify(orderRepository).findById(UUID.fromString(TEST_UUID));
        verify(orderRepository).delete(order);
    }

    @Test
    void givenNonExistingOrderId_whenDeleteOrder_thenReturnsNotFound() {
        when(orderRepository.findById(UUID.fromString(TEST_UUID))).thenReturn(Optional.empty());

        assertThrows(OrderException.class, () -> orderService.deleteOrder(TEST_UUID));
        verify(orderRepository).findById(UUID.fromString(TEST_UUID));
        verify(orderRepository, never()).delete(any());
    }
}