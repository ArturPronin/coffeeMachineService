package test.example.coffeemachineservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import test.example.coffeemachineservice.controller.impl.StatisticControllerImpl;
import test.example.coffeemachineservice.dto.request.PeriodRequestDto;
import test.example.coffeemachineservice.dto.response.DrinkResponseDto;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.exception.DrinkException;
import test.example.coffeemachineservice.exception.OrderException;
import test.example.coffeemachineservice.service.DrinkService;
import test.example.coffeemachineservice.service.OrderService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.example.coffeemachineservice.constant.ApplicationConstant.INCOMING_PARAMETER_MISSING_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.ORDERS_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.constant.ApplicationConstant.POPULAR_DRINK_NOT_FOUND_MESSAGE;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createDrinkResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createListOrderResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createOrderResponseDto;
import static test.example.coffeemachineservice.data.CoffeeMachineData.createPeriodRequestDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StatisticControllerImpl.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DrinkService drinkService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v1/statistic";

    @Test
    void testGetPopularDrink_thenReturnResponseDto() throws Exception {
        DrinkResponseDto responseDto = createDrinkResponseDto();
        when(drinkService.getPopularDrink()).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/drink/popular"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(drinkService, times(1)).getPopularDrink();
    }

    @Test
    void givenNonExistingPopularDrink_whenGetPopularDrink_thenReturnNotFound() throws Exception {
        doThrow(new DrinkException(NOT_FOUND, POPULAR_DRINK_NOT_FOUND_MESSAGE)).when(drinkService).getPopularDrink();

        mockMvc.perform(get(baseUrl + "/drink/popular"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(POPULAR_DRINK_NOT_FOUND_MESSAGE));

        verify(drinkService, times(1)).getPopularDrink();
    }

    @Test
    void testGetAllOrders_thenReturnResponseDto() throws Exception {
        List<OrderResponseDto> responseDto = createListOrderResponseDto(createOrderResponseDto());

        when(orderService.getAllOrders()).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/order/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void givenNonExistingOrder_whenGetAllOrders_thenReturnNotFound() throws Exception {
        doThrow(new OrderException(NOT_FOUND, ORDERS_NOT_FOUND_MESSAGE))
                .when(orderService).getAllOrders();

        mockMvc.perform(get(baseUrl + "/order/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ORDERS_NOT_FOUND_MESSAGE));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrdersForToday_thenReturnResponseDto() throws Exception {
        List<OrderResponseDto> responseDto = createListOrderResponseDto(createOrderResponseDto());

        when(orderService.getOrdersForToday()).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/order/today"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(orderService, times(1)).getOrdersForToday();
    }

    @Test
    void givenNonExistingOrder_whenGetOrdersForToday_thenReturnNotFound() throws Exception {
        doThrow(new OrderException(NOT_FOUND, ORDERS_NOT_FOUND_MESSAGE))
                .when(orderService).getOrdersForToday();

        mockMvc.perform(get(baseUrl + "/order/today"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ORDERS_NOT_FOUND_MESSAGE));

        verify(orderService, times(1)).getOrdersForToday();
    }

    @Test
    void testGetOrdersForCurrentWeek_thenReturnResponseDto() throws Exception {
        List<OrderResponseDto> responseDto = createListOrderResponseDto(createOrderResponseDto());

        when(orderService.getOrdersForCurrentWeek()).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/order/week"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(orderService, times(1)).getOrdersForCurrentWeek();
    }

    @Test
    void givenNonExistingOrder_whenGetOrdersForCurrentWeek_thenReturnNotFound() throws Exception {
        doThrow(new OrderException(NOT_FOUND, ORDERS_NOT_FOUND_MESSAGE))
                .when(orderService).getOrdersForCurrentWeek();

        mockMvc.perform(get(baseUrl + "/order/week"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ORDERS_NOT_FOUND_MESSAGE));

        verify(orderService, times(1)).getOrdersForCurrentWeek();
    }

    @Test
    void testGetOrdersForPeriod_thenReturnResponseDto() throws Exception {
        PeriodRequestDto requestDto = createPeriodRequestDto();
        List<OrderResponseDto> responseDto = createListOrderResponseDto(createOrderResponseDto());

        when(orderService.getOrdersForPeriod(any(PeriodRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post(baseUrl + "/order/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(orderService, times(1)).getOrdersForPeriod(any(PeriodRequestDto.class));
    }

    @Test
    void givenInvalidIncomingParameters_whenGetOrdersForPeriod_thenReturnBadRequest() throws Exception {
        PeriodRequestDto requestDto = createPeriodRequestDto();

        doThrow(new OrderException(INCOMING_PARAMETER_MISSING_MESSAGE))
                .when(orderService).getOrdersForPeriod(any());

        mockMvc.perform(post(baseUrl + "/order/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INCOMING_PARAMETER_MISSING_MESSAGE));

        verify(orderService, never()).getOrdersForPeriod(requestDto);
    }

    @Test
    void givenNonExistOrder_whenGetOrdersForPeriod_thenReturnNotFound() throws Exception {
        PeriodRequestDto requestDto = createPeriodRequestDto();

        doThrow(new OrderException(NOT_FOUND, ORDERS_NOT_FOUND_MESSAGE))
                .when(orderService).getOrdersForPeriod(any());

        mockMvc.perform(post(baseUrl + "/order/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ORDERS_NOT_FOUND_MESSAGE));

        verify(orderService, never()).getOrdersForPeriod(requestDto);
    }
}