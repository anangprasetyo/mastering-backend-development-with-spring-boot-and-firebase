package id.backend.session5_2.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentTokenPostRequestDTO {
    private int id;

    @NotNull(message = "Student ID is required")
    private int student_id;

    @NotBlank(message = "Token is required")
    private String token;
}