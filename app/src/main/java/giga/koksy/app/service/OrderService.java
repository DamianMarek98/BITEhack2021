package giga.koksy.app.service;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.enumerations.OrderType;
import giga.koksy.app.mappers.OrderMapper;
import giga.koksy.app.model.Order;
import giga.koksy.app.model.User;
import giga.koksy.app.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Optional<Order> findById(@NonNull Long id) {
        return orderRepository.findById(id);
    }

    public List<OrderDto> findUserOrders(@NonNull Long userId) {
        return OrderMapper.mapOrdersDto(orderRepository.findUserOrders(userId));
    }

    public List<OrderDto> findCreatedUserOrders(@NonNull Long userId) {
        return OrderMapper.mapOrdersDto(orderRepository.findCreatedUserOrders(userId));
    }

    public void updateOrder(@NonNull Order order) {
        orderRepository.saveAndFlush(order);
    }

    public List<OrderDto> findUnassignedOrders(@NonNull Long userId) {
        return orderRepository.findUnassignedOrders(userId, PageRequest.of(0, 10)).stream().map(OrderMapper::map).collect(Collectors.toList());
    }

    public List<OrderDto> findUnassignedOrders(@NonNull Long userId, @NonNull OrderType orderType) {
        return orderRepository.findUnassignedOrders(userId, orderType, PageRequest.of(0, 10)).stream().map(OrderMapper::map).collect(Collectors.toList());
    }

    public void addOrder(@NonNull User user, @NonNull OrderDto orderDto) {
        Order order = new Order();
        order.setName(orderDto.getName());
        order.setDescription(orderDto.getDescription());
        order.setOrderType(OrderType.valueOf(orderDto.getOrderType()));
        order.setCreator(user);
        orderRepository.saveAndFlush(order);
    }

    public boolean finishOrder(@NonNull User user, @NonNull Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getCreator().equals(user)) {
                order.setFinished(true);
                orderRepository.saveAndFlush(order);
                return true;
            }
        }

        return false;
    }

}
