package id.project.education.dto.enrollment;

import id.project.education.dto.course.CourseGetDTO;
import id.project.education.dto.user.UserGetDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentGetDTO {
    private Long id;
    private UserGetDTO student;
    private CourseGetDTO course;
}