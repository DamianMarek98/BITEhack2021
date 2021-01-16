package giga.koksy.app.controllers;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.model.User;
import giga.koksy.app.service.OrderService;
import giga.koksy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(@Autowired OrderService orderService, @Autowired UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping(value = "/user-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDto> userOrders(@RequestParam(value = "token") String token) {
        Optional<User> user = userService.findUserByUsername(token);
        if (user.isPresent()) {
            return orderService.findUserOrders(user.get().getId());
        }

        return Collections.emptyList();
    }
}
