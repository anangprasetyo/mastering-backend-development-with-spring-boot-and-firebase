package id.backend.session2_2.dto.student;

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
public class StudentPostRequestDTO {
    private int id;

    @NotBlank(message = "Name tidak boleh kosong!")
    @Size(max = 50, message = "Jumlah karakter Name tidak boleh lebih dari 50")
    private String name;

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Class tidak boleh kosong")
    private String classes;
}
