package giga.koksy.app.service;

import giga.koksy.app.model.User;
import giga.koksy.app.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Order {

    private final Order order;

    public Optional<Order> findById(@NonNull Long id) {
        return order.findById(id);
    }
}
