package id.project.education.dto.user;

import java.util.List;

import id.project.education.model.Enrollment;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetDTO {
    private Long id;
    private String name;
    private String email;
    private String role; // "STUDENT" atau "TEACHER"
    private List<Enrollment> enrollments;
}