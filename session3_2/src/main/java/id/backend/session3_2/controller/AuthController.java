package id.backend.session3_2.controller;

import id.backend.session3_2.dto.student.StudentLoginRequestDTO;
import id.backend.session3_2.dto.student.StudentLoginResponseDTO;
import id.backend.session3_2.utils.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired  
    private UserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public StudentLoginResponseDTO authenticateUser(@RequestBody StudentLoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtTokenUtil.generateToken(userDetails);

            return new StudentLoginResponseDTO(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }

    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getAuthorities());
    }

    @PostMapping("/logout")
    public String logoutUser() {
        SecurityContextHolder.clearContext();
        return "User logged out successfully";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
