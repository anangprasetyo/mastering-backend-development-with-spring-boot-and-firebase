package id.backend.session_5.dto.user;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}
