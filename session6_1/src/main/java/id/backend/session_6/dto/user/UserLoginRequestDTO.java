package id.backend.session_6.dto.user;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}
