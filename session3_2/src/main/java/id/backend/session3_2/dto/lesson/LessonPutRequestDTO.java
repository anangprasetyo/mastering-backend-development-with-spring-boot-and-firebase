package id.backend.session3_2.dto.lesson;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPutRequestDTO {

    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;
    private Integer student_id;
}