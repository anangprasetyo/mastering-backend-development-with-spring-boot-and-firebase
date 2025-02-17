package id.backend.session5_2.dto.student;

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
public class StudentPutRequestDTO {
    @Size(max = 50, message = "Jumlah karakter Name tidak boleh lebih dari 50")
    private String name;

    @UniqueElements(message = "Email sudah tersedia. Gunakan email lain")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private String classes;
}
