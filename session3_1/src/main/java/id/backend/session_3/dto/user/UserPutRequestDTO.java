package id.backend.session_3.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import id.backend.session_3.model.Roles;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPutRequestDTO {
    @Size(max = 50, message = "Name must not exceed 500 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}