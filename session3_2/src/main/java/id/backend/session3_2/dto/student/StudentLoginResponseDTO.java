package id.backend.session3_2.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentLoginResponseDTO {
    private String token;
}
