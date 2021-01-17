package giga.koksy.app.mappers;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.enumerations.OrderType;
import giga.koksy.app.model.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {
    public static OrderDto map(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setName(order.getName());
        orderDto.setOrderType(order.getOrderType().toString());
        orderDto.setDescription(order.getDescription());

        return orderDto;
    }

    public static List<OrderDto> mapOrdersDto(List<Order> orders) {
        return orders.stream().map(OrderMapper::map).collect(Collectors.toList());
    }

    public static Order map(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setName(orderDto.getName());
        order.setOrderType(OrderType.valueOf(orderDto.getOrderType()));
        order.setDescription(orderDto.getDescription());

        return order;
    }

    public static List<Order> mapOrders(List<OrderDto> ordersDto) {
        return ordersDto.stream().map(OrderMapper::map).collect(Collectors.toList());
    }
}
