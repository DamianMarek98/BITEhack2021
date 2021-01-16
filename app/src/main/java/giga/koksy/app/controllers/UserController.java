package giga.koksy.app.controllers;

import giga.koksy.app.dto.UserDto;
import giga.koksy.app.model.User;
import giga.koksy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private String login(@RequestBody UserDto userDto) {
        Optional<User> user = userService.findUserByDetails(userDto.getUsername(), userDto.getPassword());
        if (user.isPresent()) {
            return user.get().getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    private String register(@RequestBody UserDto userDto) {
        if (userService.addUser(userDto)) {
            return userDto.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given username exists");
        }
    }
}
