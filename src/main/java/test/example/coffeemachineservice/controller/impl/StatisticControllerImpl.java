package test.example.coffeemachineservice.controller.impl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import static test.example.coffeemachineservice.constant.ApplicationConstant.INCORRECT_UUID_FORMAT_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.REGEXP_UUID;

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
        return ResponseEntity.ok(orderService.getOrdersForPeriod(requestDto));
    }

    @Override
    @DeleteMapping("/order/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable("orderId")
            @Schema(description = "orderId", example = "1568b827-4f03-4185-b622-64a5b9f46be3")
            @Valid @Pattern(regexp = REGEXP_UUID, message = INCORRECT_UUID_FORMAT_MESSAGE) String orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }
}