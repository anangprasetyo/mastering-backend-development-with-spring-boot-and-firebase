package id.backend.session_1.service;

import java.util.List;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(int id, User user) {
        if (!userRepository.findById(id).isPresent()) {
            return Optional.empty();
        }
        user.setId(id);
        return Optional.of(userRepository.save(user));
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteById(id);
    }
}
