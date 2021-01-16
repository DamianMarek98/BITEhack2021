package giga.koksy.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderService order;

    public Optional<OrderService> findById(@NonNull Long id) {
        return order.findById(id);
    }
}
