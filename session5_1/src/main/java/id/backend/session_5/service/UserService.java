package id.backend.session_5.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import id.backend.session_5.dto.task.TaskGetRequestDTO;
import id.backend.session_5.dto.user.UserGetRequestDTO;
import id.backend.session_5.dto.user.UserPostRequestDTO;
import id.backend.session_5.dto.user.UserPutRequestDTO;
import id.backend.session_5.dto.user.UserTokenGetRequestDTO;
import id.backend.session_5.model.User;
import id.backend.session_5.model.UserToken;
import id.backend.session_5.repository.UserRepository;
import id.backend.session_5.repository.UserTokenRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
        if (userDTO.getRole() == null) {
            throw new InvalidDataException("Role cannot be null");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(userDTO.getRole());
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
        if (userDTO.getRole() != null) {
            throw new InvalidDataException("Role cannot be null");
        }

        if (Objects.nonNull(userDTO.getName())) {
            user.setName(userDTO.getName());
        }

        if (Objects.nonNull(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }

        if (Objects.nonNull(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (Objects.nonNull(userDTO.getRole())) {
            user.setRoles(userDTO.getRole());
        }
        User savedUser = userRepository.save(user);
        return convertToGetRequestDTO(savedUser);
    }

    public UserTokenGetRequestDTO convertUserTokenToGetRequestDTO(UserToken userToken) {
        User user = userRepository.findById(userToken.getUser().getId()).orElse(null);
        return UserTokenGetRequestDTO.builder()
                .id(userToken.getId())
                .user(convertToGetRequestDTO(user))
                .token(userToken.getToken()).build();
    }

    @Transactional
    public UserTokenGetRequestDTO saveToken(int userId, String token) {
        UserToken userToken = new UserToken();
        Optional<UserToken> existingToken = userTokenRepository.findByUserId(userId);
        if (existingToken.isPresent()) {
            existingToken.get().setToken(token);
            userTokenRepository.save(existingToken.get());

            userToken = existingToken.get();
        } else {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", userId));

            userToken = new UserToken();
            userToken.setUser(user);
            userToken.setToken(token);
            userTokenRepository.save(userToken);
        }

        return convertUserTokenToGetRequestDTO(userToken);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
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