package id.backend.session_6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import id.backend.session_6.dto.user.UserLoginRequestDTO;
import id.backend.session_6.dto.user.UserLoginResponseDTO;
import id.backend.session_6.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public UserLoginResponseDTO signup(@RequestBody UserLoginRequestDTO signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public UserLoginResponseDTO authenticateUser(@RequestBody UserLoginRequestDTO loginRequest) {
        try {
            return authService.login(loginRequest);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public String logoutFirebaseUser(@RequestHeader("Authorization") String token) {
        try {
            String idToken = token.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            FirebaseAuth.getInstance().revokeRefreshTokens(decodedToken.getUid());
            return "Firebase user logged out successfully";
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Failed to logout Firebase user", e);
        }
    }
}
