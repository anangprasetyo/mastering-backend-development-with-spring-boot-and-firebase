package id.project.education.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.auth.FirebaseAuthException;

import id.project.education.dto.user.UserGetDTO;
import id.project.education.dto.user.UserPostDTO;
import id.project.education.dto.user.UserPutDTO;
import id.project.education.exception.UserNotFoundException;
import id.project.education.model.User;
import id.project.education.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, FirebaseService firebaseService,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserGetDTO convertToUserGetDTO(User user) {
        return UserGetDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enrollments(null).build();
    }

    @Transactional(readOnly = true)
    public List<UserGetDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserGetDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserGetDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return convertToUserGetDTO(user);
    }

    @Transactional
    public UserGetDTO registerUser(UserPostDTO userPostDTO) throws FirebaseAuthException {
        String firebaseUid = firebaseService.createFirebaseUser(userPostDTO.getEmail(), userPostDTO.getPassword());

        User user = new User();
        user.setName(userPostDTO.getName());
        user.setEmail(userPostDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userPostDTO.getPassword())); // Hash password sebelum menyimpan
        user.setRole(userPostDTO.getRole());
        user.setFirebaseUid(firebaseUid);
        user.setTokens(userPostDTO.getTokens());

        userRepository.save(user);
        return convertToUserGetDTO(user);
    }

    @Transactional
    public UserGetDTO updateUser(Long id, UserPutDTO userPutDTO) throws FirebaseAuthException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        boolean updateFirebase = false;

        if (userPutDTO.getName() != null) {
            user.setName(userPutDTO.getName());
        }
        if (userPutDTO.getEmail() != null) {
            user.setEmail(userPutDTO.getEmail());
            updateFirebase = true;
        }
        if (userPutDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userPutDTO.getPassword())); // Hash password sebelum menyimpan
            updateFirebase = true;
        }
        if (userPutDTO.getRole() != null) {
            user.setRole(userPutDTO.getRole());
        }
        if (userPutDTO.getTokens() != null) {
            user.setTokens(userPutDTO.getTokens());
        }

        userRepository.save(user);

        if (updateFirebase) {
            firebaseService.updateFirebaseUser(user.getFirebaseUid(), userPutDTO.getEmail(), userPutDTO.getPassword());
        }

        return convertToUserGetDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) throws FirebaseAuthException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Delete user from Firebase Authentication
        firebaseService.deleteFirebaseUser(user.getFirebaseUid());

        // Delete user from the database
        userRepository.deleteById(id);
    }
}