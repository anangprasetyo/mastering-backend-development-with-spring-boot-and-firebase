package id.backend.session6_2.dto.user;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}
