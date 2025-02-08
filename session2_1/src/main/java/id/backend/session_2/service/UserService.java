package id.backend.session_2.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import id.backend.session_2.dto.task.TaskGetRequestDTO;
import id.backend.session_2.dto.user.UserGetRequestDTO;
import id.backend.session_2.dto.user.UserPostRequestDTO;
import id.backend.session_2.dto.user.UserPutRequestDTO;
import id.backend.session_2.model.Roles;
import id.backend.session_2.model.User;
import id.backend.session_2.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    // convert User to get request DTO
    public UserGetRequestDTO convertToGetRequestDTO(User user) {
        List<TaskGetRequestDTO> taskDTOs = user.getTasks() != null
                ? user.getTasks().stream().map(taskService::convertToGetRequestDTO).collect(Collectors.toList())
                : Collections.emptyList();
        return UserGetRequestDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRoles().name())
                .tasks(taskDTOs).build();
    }

    // gett all user
    public List<UserGetRequestDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // get user by id
    public UserGetRequestDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User dengan id: " + id + " tidak ditemukan"));
        return this.convertToGetRequestDTO(user);
    }

    // create new user
    @Transactional
    public UserGetRequestDTO createUser(UserPostRequestDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new InvalidDataException("Name cannot be null or empty");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new InvalidDataException("Email cannot be null or empty");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new InvalidDataException("Password cannot be null or empty");
        }
        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            throw new InvalidDataException("Role cannot be null or empty");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(Roles.valueOf(userDTO.getRole()));
        User savedUser = userRepository.save(user);
        return convertToGetRequestDTO(savedUser);
    }

    // update user
    @Transactional
    public UserGetRequestDTO updateUser(int id, UserPutRequestDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (userDTO.getName() != null && userDTO.getName().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty");
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().isEmpty()) {
            throw new InvalidDataException("Email cannot be empty");
        }
        if (userDTO.getPassword() != null && userDTO.getPassword().isEmpty()) {
            throw new InvalidDataException("Password cannot be empty");
        }
        if (userDTO.getRole() != null && userDTO.getRole().isEmpty()) {
            throw new InvalidDataException("Role cannot be empty");
        }

        if (Objects.nonNull(userDTO.getName())) {
            user.setName(userDTO.getName());
        }

        if (Objects.nonNull(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }

        if (Objects.nonNull(userDTO.getPassword())) {
            user.setPassword(userDTO.getPassword());
        }

        if (Objects.nonNull(userDTO.getRole())) {
            user.setRoles(Roles.valueOf(userDTO.getRole()));
        }
        User savedUser = userRepository.save(user);
        return convertToGetRequestDTO(savedUser);
    }

    public Map<String, Object> deleteUser(int id) {
        try {
            User user = userRepository.findById(id).get();
            userRepository.delete(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil menghapus user");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menghapus task");
            return response;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class InvalidDataException extends RuntimeException {
        public InvalidDataException(String message) {
            super(message);
        }
    }
}
