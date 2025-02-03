package id.backend.session_3.dto.user;

import org.hibernate.validator.constraints.UniqueElements;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPutRequestDTO {
    @Size(max = 100, message = "Jumlah karakter Name tidak boleh lebih dari 100")
    private String name;

    @UniqueElements(message = "Email sudah tersedia. Gunakan email lain")
    private String email;

    private String password;
    private String role;
}
