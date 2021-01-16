package giga.koksy.app.mappers;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto map(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setName(order.getName());
        orderDto.setOrderType(order.getOrderType().toString());
        orderDto.setDescription(order.getDescription());

        return orderDto;
    }

    public static List<OrderDto> map(List<Order> orders) {
        return orders.stream().map(OrderMapper::map).collect(Collectors.toList());
    }

    public static Order map(OrderDto orderDto) {

    }
}
