package giga.koksy.app.controllers;

import giga.koksy.app.dto.UserDto;
import giga.koksy.app.service.AuthenticationService;
import giga.koksy.app.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    public UserController(@Autowired UserService userService, @Autowired AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Object> login(@RequestBody UserDto userDto) {
        UserDetails userDetails = authenticationService.loadUserByUsername(userDto.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword());
        if (isPasswordMatch) {
            JSONObject json = new JSONObject();
            json.put("value", userDetails.getUsername());
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Object> register(@RequestBody UserDto userDto) {
        if (userService.addUser(userDto)) {
            JSONObject json = new JSONObject();
            json.put("value", userDto.getUsername());
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given username exists");
        }
    }
}
