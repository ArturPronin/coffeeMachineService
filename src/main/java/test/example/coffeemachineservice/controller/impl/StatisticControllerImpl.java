package test.example.coffeemachineservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.example.coffeemachineservice.controller.StatisticController;
import test.example.coffeemachineservice.dto.request.PeriodRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.service.DrinkService;
import test.example.coffeemachineservice.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistic")
public class StatisticControllerImpl implements StatisticController {

    private final DrinkService drinkService;

    private final OrderService orderService;

    @Override
    @GetMapping("/drink/popular")
    public ResponseEntity<DrinkResponseDto> getPopularDrink() {
        return ResponseEntity.ok(drinkService.getPopularDrink());
    }

    @Override
    @GetMapping("/order/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Override
    @GetMapping("/order/today")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForToday() {
        return ResponseEntity.ok(orderService.getOrdersForToday());
    }

    @Override
    @GetMapping("/order/week")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForCurrentWeek() {
        return ResponseEntity.ok(orderService.getOrdersForCurrentWeek());
    }

    @Override
    @PostMapping("/order/period")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForPeriod(@Valid @RequestBody PeriodRequestDto requestDto) {
        return ResponseEntity.ok(orderService.getOrdersForPeriod(requestDto.getStartDate(), requestDto.getEndDate()));
    }
}