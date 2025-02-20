package id.project.education.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDTO {
    private String firebaseUid;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 20, message = "Role must be less than 20 characters")
    private String role; // "STUDENT" atau "TEACHER"

    private String tokens;
}