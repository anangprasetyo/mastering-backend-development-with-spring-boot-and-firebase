package id.project.education.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.project.education.dto.course.CourseGetDTO;
import id.project.education.dto.course.CoursePostDTO;
import id.project.education.dto.course.CoursePutDTO;
import id.project.education.dto.user.UserGetDTO;
import id.project.education.exception.CourseNotFoundException;
import id.project.education.model.Course;
import id.project.education.model.User;
import id.project.education.repository.CourseRepository;
import id.project.education.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public CourseGetDTO convertToCourseGetDTO(Course course) {
        User teacher = userRepository.findByIdAndRole(course.getTeacher().getId(), "TEACHER").orElse(null);
        UserGetDTO teacherDTO = UserGetDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .role(teacher.getRole())
                .enrollments(null)
                .build();

        return CourseGetDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacherId(course.getTeacher().getId())
                .teacher(teacherDTO).build();
    }

    @Transactional
    public CourseGetDTO createCourse(CoursePostDTO coursePostDTO) {
        // Check if the teacher exists
        User teacher = userRepository.findByIdAndRole(coursePostDTO.getTeacherId(), "TEACHER")
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + coursePostDTO.getTeacherId()));

        Course course = new Course();
        course.setTitle(coursePostDTO.getTitle());
        course.setDescription(coursePostDTO.getDescription());
        course.setTeacher(teacher);

        courseRepository.save(course);

        return convertToCourseGetDTO(course);
    }

    @Transactional(readOnly = true)
    public List<CourseGetDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseGetDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseGetDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));
        return convertToCourseGetDTO(course);
    }

    @Transactional
    public CourseGetDTO updateCourse(Long id, CoursePutDTO coursePutDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));

        // Check if the teacher exists
        User teacher = userRepository.findByIdAndRole(coursePutDTO.getTeacherId(), "TEACHER")
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + coursePutDTO.getTeacherId()));

        if (coursePutDTO.getTitle() != null) {
            course.setTitle(coursePutDTO.getTitle());
        }
        if (coursePutDTO.getDescription() != null) {
            course.setDescription(coursePutDTO.getDescription());
        }
        if (coursePutDTO.getTeacherId() != null) {
            course.setTeacher(teacher);
        }

        Course updatedCourse = courseRepository.save(course);
        return convertToCourseGetDTO(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));
        courseRepository.deleteById(id);
    }
}