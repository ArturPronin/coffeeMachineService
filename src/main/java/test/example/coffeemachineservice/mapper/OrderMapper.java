package test.example.coffeemachineservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.example.coffeemachineservice.dto.response.OrderResponseDto;
import test.example.coffeemachineservice.persistent.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "drinkName", source = "drink.drinkName")
    OrderResponseDto mapToOrderResponseDto(Order order);
}