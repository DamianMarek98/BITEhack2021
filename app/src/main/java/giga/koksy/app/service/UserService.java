package giga.koksy.app.service;

import giga.koksy.app.dto.UserDto;
import giga.koksy.app.model.User;
import giga.koksy.app.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByDetails(@NonNull String username, @NonNull String password) {
        return userRepository.findByDetails(username, password);
    }

    public boolean addUser(UserDto userDto) {
        Optional<User> duplicatedUser = userRepository.findByUsername(userDto.getUsername());
        if (duplicatedUser.isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.saveAndFlush(user);
        return true;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
