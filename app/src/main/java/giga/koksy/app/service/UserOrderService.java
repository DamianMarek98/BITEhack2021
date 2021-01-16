package giga.koksy.app.service;

import giga.koksy.app.model.UserOrder;
import giga.koksy.app.repository.UserOrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final UserOrderRepository userOrderRepository;

    public Optional<UserOrder> findById(@NonNull Long id) {
        return userOrderRepository.findById(id);
    }
}
