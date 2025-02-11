package id.backend.session3_2.dto.student;

import java.util.List;

import id.backend.session3_2.dto.lesson.LessonGetRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentGetRequestDTO {
    private int id;
    private String name;
    private String email;
    private String classes;
    private List<LessonGetRequestDTO> lessons;
}
