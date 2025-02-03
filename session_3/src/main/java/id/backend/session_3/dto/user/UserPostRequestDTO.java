package id.backend.session_3.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostRequestDTO {
    private int id;

    @NotBlank(message = "Name tidak boleh kosong!")
    @Size(max = 100, message = "Jumlah karakter Name tidak boleh lebih dari 100")
    private String name;

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;

    @NotBlank(message = "Role tidak boleh kosong")
    private String role;
}
