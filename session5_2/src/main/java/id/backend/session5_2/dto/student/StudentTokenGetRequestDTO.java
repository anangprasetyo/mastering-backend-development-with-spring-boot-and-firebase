package id.backend.session5_2.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentTokenGetRequestDTO {
    private int id;
    private StudentGetRequestDTO student;
    private String token;
}
