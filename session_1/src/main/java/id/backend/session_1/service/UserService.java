package id.backend.session_1.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import id.backend.session_1.model.User;
import id.backend.session_1.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
