package id.backend.session_1.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import id.backend.session_1.model.User;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private int nextId = 3; // Since we start with 2 users

    public UserRepository() {
        users.add(new User(1, "John Doe", "john.doe@example.com"));
        users.add(new User(2, "Jane Smith", "jane.smith@example.com"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == 0) {
            // New user
            user.setId(nextId++);
            users.add(user);
        } else {
            // Update existing user
            deleteById(user.getId());
            users.add(user);
        }
        return user;
    }

    public boolean deleteById(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

}
