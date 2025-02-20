package id.project.education.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentPostDTO {
    @NotNull(message = "Student ID is mandatory")
    private Long studentId;

    @NotNull(message = "Course ID is mandatory")
    private Long courseId;
}