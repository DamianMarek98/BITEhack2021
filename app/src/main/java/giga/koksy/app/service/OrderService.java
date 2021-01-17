package giga.koksy.app.service;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.mappers.OrderMapper;
import giga.koksy.app.model.Order;
import giga.koksy.app.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<OrderDto> findCreatedUserOrders(@NonNull Long userId){
        return OrderMapper.mapOrdersDto(orderRepository.findCreatedUserOrders(userId));
    }

}
