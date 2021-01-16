package giga.koksy.app.service;

import giga.koksy.app.model.Order;
import giga.koksy.app.model.User;
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

    public boolean addUserOrder(User user, Order order, boolean accepted) {
        if (userOrderRepository.findUserOrderByDetails(user.getId(), order.getId()).isPresent()) {
            return false;
        }

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setOrder(order);
        userOrder.setIsAccepted(accepted);
        userOrderRepository.saveAndFlush(userOrder);
        return true;
    }
}
