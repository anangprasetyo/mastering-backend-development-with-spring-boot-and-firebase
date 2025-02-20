package id.project.education.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import id.project.education.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findByIdAndRole(Long id, String role);
}
