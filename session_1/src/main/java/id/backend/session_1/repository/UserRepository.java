package id.backend.session_1.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import id.backend.session_1.model.User;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new User(1, "John Doe", "john.doe@example.com"));
        users.add(new User(2, "Jane Smith", "jane.smith@example.com"));
    }

    public Optional<User> findById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }
}
