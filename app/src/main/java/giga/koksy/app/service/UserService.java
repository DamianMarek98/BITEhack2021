package giga.koksy.app.service;

import giga.koksy.app.model.User;
import giga.koksy.app.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(@NonNull Long id) {
        return userRepository.findById(id);
    }

}
