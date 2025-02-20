package id.project.education.dto.course;

import id.project.education.dto.user.UserGetDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseGetDTO {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private UserGetDTO teacher;
}
