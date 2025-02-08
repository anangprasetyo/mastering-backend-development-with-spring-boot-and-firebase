package id.backend.session2_2.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonGetRequestDTO {
    private int id;
    private String name;
    private Integer student_id;
}