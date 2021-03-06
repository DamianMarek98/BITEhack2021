package giga.koksy.app.controllers;

import giga.koksy.app.dto.UserDto;
import giga.koksy.app.model.User;
import giga.koksy.app.service.AuthenticationService;
import giga.koksy.app.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class UserController {

    public static final String JSON_VALUE = "value";
    private final UserService userService;

    private final AuthenticationService authenticationService;

    public UserController(@Autowired UserService userService, @Autowired AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<JSONObject> login(@RequestBody UserDto userDto) {
        UserDetails userDetails = authenticationService.loadUserByUsername(userDto.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword());
        if (isPasswordMatch) {
            JSONObject json = new JSONObject();
            json.put(JSON_VALUE, userDetails.getUsername());
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<JSONObject> register(@RequestBody UserDto userDto) {
        if (userService.addUser(userDto)) {
            JSONObject json = new JSONObject();
            json.put(JSON_VALUE, userDto.getUsername());
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given username exists");
        }
    }

    @GetMapping(value = "/user-points", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<JSONObject> userPoints(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Optional<User> user = userService.findUserByUsername(token);
        JSONObject json = new JSONObject();
        if (user.isPresent()) {
            json.put(JSON_VALUE, user.get().getPoints());
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        json.put(JSON_VALUE, 0);
        return new ResponseEntity<>(json, HttpStatus.NOT_FOUND);
    }
}
