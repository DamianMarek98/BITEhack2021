package giga.koksy.app.controllers;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.dto.UserOrderDto;
import giga.koksy.app.model.Order;
import giga.koksy.app.model.User;
import giga.koksy.app.service.OrderService;
import giga.koksy.app.service.UserOrderService;
import giga.koksy.app.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final UserOrderService userOrderService;

    public OrderController(@Autowired OrderService orderService, @Autowired UserService userService, @Autowired UserOrderService userOrderService) {
        this.orderService = orderService;
        this.userService = userService;
        this.userOrderService = userOrderService;
    }

    @GetMapping(value = "/user-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userOrders(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        JSONObject json = new JSONObject();
        json.put("value", user.isPresent() ? orderService.findUserOrders(user.get().getId()) : Collections.emptyList());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/save-user-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String saveUserOrder(HttpServletRequest request, @RequestBody UserOrderDto userOrderDto) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        if (user.isPresent()) {
            Optional<Order> order = orderService.findById(userOrderDto.getOrderId());
            if (order.isPresent()) {
                boolean status = userOrderService.addUserOrder(user.get(), order.get(), userOrderDto.isAccepted());
                if (status) {
                    Order toUpdate = order.get();
                    toUpdate.setAccepted(true);
                    orderService.updateOrder(toUpdate);
                    return "operation successful";
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "UserOrder exists");
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Order not found");
    }

    @PostMapping(value = "/save-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String saveOrder(HttpServletRequest request, @RequestBody OrderDto orderDto) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        if (user.isPresent()) {
            orderService.addOrder(user.get(), orderDto);
            return "operation successful";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping(value = "/user-created-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createdUserOrders(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        JSONObject json = new JSONObject();
        json.put("value", user.isPresent() ? orderService.findCreatedUserOrders(user.get().getId()) : Collections.emptyList());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/unassigned-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnassignedOrders(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        JSONObject json = new JSONObject();
        json.put("value", user.isPresent() ? orderService.findUnassignedOrders(user.get().getId()) : Collections.emptyList());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
