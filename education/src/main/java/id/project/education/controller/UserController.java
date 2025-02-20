package id.project.education.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.auth.FirebaseAuthException;

import id.project.education.dto.user.UserGetDTO;
import id.project.education.dto.user.UserPostDTO;
import id.project.education.dto.user.UserPutDTO;
import id.project.education.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<UserGetDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable Long id) {
        UserGetDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserGetDTO> registerUser(@RequestBody UserPostDTO user) throws FirebaseAuthException {
        UserGetDTO userGetDTO = userService.registerUser(user);
        return ResponseEntity.ok(userGetDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDTO> updateUser(@PathVariable Long id, @RequestBody UserPutDTO userPutDTO)
            throws FirebaseAuthException {
        UserGetDTO userGetDTO = userService.updateUser(id, userPutDTO);
        return ResponseEntity.ok(userGetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws FirebaseAuthException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
