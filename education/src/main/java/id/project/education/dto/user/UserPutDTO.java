package id.project.education.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPutDTO {
    @NotBlank(message = "Firebase UID is mandatory")
    @Size(max = 255, message = "Firebase UID must be less than 255 characters")
    private String firebaseUid;

    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Size(min = 6, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    @Size(max = 20, message = "Role must be less than 20 characters")
    private String role; // "STUDENT" atau "TEACHER"

    private String tokens;
}
