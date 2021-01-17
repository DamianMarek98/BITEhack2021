package giga.koksy.app.controllers;

import giga.koksy.app.dto.OrderDto;
import giga.koksy.app.dto.UserOrderDto;
import giga.koksy.app.enumerations.OrderType;
import giga.koksy.app.model.Order;
import giga.koksy.app.model.User;
import giga.koksy.app.model.UserOrder;
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

    public static final String JSON_VALUE = "value";
    private final OrderService orderService;
    private final UserService userService;
    private final UserOrderService userOrderService;
    private static final String SUCCESSFUL_OPERATION = "operation successful";

    public OrderController(@Autowired OrderService orderService, @Autowired UserService userService, @Autowired UserOrderService userOrderService) {
        this.orderService = orderService;
        this.userService = userService;
        this.userOrderService = userOrderService;
    }

    @GetMapping(value = "/user-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> userOrders(HttpServletRequest request) {
        Optional<User> user = extractUserFromToken(request);
        JSONObject json = new JSONObject();
        json.put(JSON_VALUE, user.isPresent() ? orderService.findUserOrders(user.get().getId()) : Collections.emptyList());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/save-user-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String saveUserOrder(HttpServletRequest request, @RequestBody UserOrderDto userOrderDto) {
        Optional<User> user = extractUserFromToken(request);
        if (user.isPresent()) {
            Optional<Order> order = orderService.findById(userOrderDto.getOrderId());
            if (order.isPresent()) {
                boolean status = userOrderService.addUserOrder(user.get(), order.get(), userOrderDto.isAccepted());
                if (status) {
                    Order toUpdate = order.get();
                    toUpdate.setAccepted(userOrderDto.isAccepted());
                    orderService.updateOrder(toUpdate);
                    return SUCCESSFUL_OPERATION;
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
        Optional<User> user = extractUserFromToken(request);
        if (user.isPresent()) {
            if (user.get().getPoints() < 1) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User has not enough points");
            }
            orderService.addOrder(user.get(), orderDto);
            User userToUpdate = user.get();
            userToUpdate.decreasePoints(1);
            userService.updateUser(userToUpdate);
            return SUCCESSFUL_OPERATION;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping(value = "/user-created-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> createdUserOrders(HttpServletRequest request) {
        Optional<User> user = extractUserFromToken(request);
        JSONObject json = new JSONObject();
        json.put(JSON_VALUE, user.isPresent() ? orderService.findCreatedUserOrders(user.get().getId()) : Collections.emptyList());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/unassigned-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> getUnassignedOrders(HttpServletRequest request, @RequestParam(required = false) String orderType) {
        Optional<User> user = extractUserFromToken(request);
        JSONObject json = new JSONObject();
        if (orderType == null) {
            json.put(JSON_VALUE, user.isPresent() ? orderService.findUnassignedOrders(user.get().getId()) : Collections.emptyList());
        } else {
            OrderType ot = OrderType.valueOf(orderType);
            json.put(JSON_VALUE, user.isPresent() ? orderService.findUnassignedOrders(user.get().getId(), ot) : Collections.emptyList());
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(value = "/finish-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JSONObject> finishOrder(HttpServletRequest request, @RequestBody OrderDto orderDto) {
        Optional<User> user = extractUserFromToken(request);
        if (user.isPresent()) {
            boolean result = orderService.finishOrder(user.get(), orderDto.getId());
            if (result) {
                Optional<UserOrder> userOrder = userOrderService.findByOrderId(orderDto.getId());
                if (userOrder.isPresent()) {
                    User userToUpdate = userOrder.get().getUser();
                    userToUpdate.incrementPoints(2);
                    userService.updateUser(userToUpdate);

                    JSONObject json = new JSONObject();
                    json.put(JSON_VALUE, orderService.findCreatedUserOrders(userToUpdate.getId()));
                    return new ResponseEntity<>(json, HttpStatus.OK);
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    private Optional<User> extractUserFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return userService.findUserByUsername(token);
    }
}
