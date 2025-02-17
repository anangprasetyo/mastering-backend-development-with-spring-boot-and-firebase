package id.backend.session_5.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import id.backend.session_5.dto.user.UserGetRequestDTO;
import id.backend.session_5.dto.user.UserPostRequestDTO;
import id.backend.session_5.dto.user.UserPutRequestDTO;
import id.backend.session_5.dto.user.UserTokenGetRequestDTO;
import id.backend.session_5.dto.user.UserTokenPostRequestDTO;
import id.backend.session_5.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // endpoint get all user
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserGetRequestDTO> getAllUser() {
        return userService.getAllUser();
    }

    // endpoint get user by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetRequestDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // endpoint create new user
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserGetRequestDTO createuser(@Valid @RequestBody UserPostRequestDTO userDto) {

        return userService.createUser(userDto);
    }

    // endpoint update user
    // patch request digunakan untuk update sebagian data
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserGetRequestDTO updateUser(@PathVariable int id, @RequestBody UserPutRequestDTO userDto) {
        return userService.updateUser(id, userDto);
    }

    // endpoint delete user
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Map<String, Object> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    // endpoint add user token
    @PostMapping(path = "/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTokenGetRequestDTO saveToken(@RequestBody UserTokenPostRequestDTO userTokenDto) {
        return userService.saveToken(userTokenDto.getUser_id(), userTokenDto.getToken());
    }
}
