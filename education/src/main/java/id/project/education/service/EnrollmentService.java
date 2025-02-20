package id.project.education.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;

import id.project.education.dto.course.CourseGetDTO;
import id.project.education.dto.enrollment.EnrollmentGetDTO;
import id.project.education.dto.enrollment.EnrollmentPostDTO;
import id.project.education.dto.enrollment.EnrollmentPutDTO;
import id.project.education.dto.user.UserGetDTO;
import id.project.education.exception.CourseNotFoundException;
import id.project.education.exception.UserNotFoundException;
import id.project.education.model.Course;
import id.project.education.model.Enrollment;
import id.project.education.model.User;
import id.project.education.repository.CourseRepository;
import id.project.education.repository.EnrollmentRepository;
import id.project.education.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
        private final EnrollmentRepository enrollmentRepository;
        private final CourseRepository courseRepository;
        private final UserRepository userRepository;
        private final NotificationService notificationService;

        public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                        UserRepository userRepository, NotificationService notificationService) {
                this.enrollmentRepository = enrollmentRepository;
                this.courseRepository = courseRepository;
                this.userRepository = userRepository;
                this.notificationService = notificationService;
        }

        public EnrollmentGetDTO convertToEnrollmentGetDTO(Enrollment enrollment) {
                UserGetDTO studentGetDTO = UserGetDTO.builder()
                                .id(enrollment.getStudent().getId())
                                .name(enrollment.getStudent().getName())
                                .email(enrollment.getStudent().getEmail())
                                .role(enrollment.getStudent().getRole())
                                .build();

                User teacher = userRepository.findByIdAndRole(
                                enrollment.getCourse().getTeacher().getId(), "TEACHER").orElse(null);

                UserGetDTO teacherGetDTO = null;
                if (teacher != null) {
                        teacherGetDTO = UserGetDTO.builder()
                                        .id(teacher.getId())
                                        .name(teacher.getName())
                                        .email(teacher.getEmail())
                                        .role(teacher.getRole())
                                        .build();
                }

                CourseGetDTO courseGetDTO = CourseGetDTO.builder()
                                .id(enrollment.getCourse().getId())
                                .title(enrollment.getCourse().getTitle())
                                .description(enrollment.getCourse().getDescription())
                                .teacherId(enrollment.getCourse().getTeacher()
                                                .getId())
                                .teacher(teacherGetDTO)
                                .build();

                return EnrollmentGetDTO.builder()
                                .id(enrollment.getId())
                                .student(studentGetDTO)
                                .course(courseGetDTO)
                                .build();
        }

        @Transactional
        public EnrollmentGetDTO createEnrollment(EnrollmentPostDTO enrollmentPostDTO)
                        throws FirebaseMessagingException {
                User student = userRepository.findByIdAndRole(enrollmentPostDTO.getStudentId(), "STUDENT")
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Student not found with ID: " + enrollmentPostDTO.getStudentId()));

                Course course = courseRepository.findById(enrollmentPostDTO.getCourseId())
                                .orElseThrow(() -> new CourseNotFoundException(
                                                "Course not found with ID: " + enrollmentPostDTO.getCourseId()));

                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);

                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

                // Send notification
                if (student.getTokens() != null) {
                        System.out.println("Sending notification to student: " + student.getName());
                        notificationService.sendNotification(student.getTokens(), "Enrollment Created",
                                        "You have been enrolled in " + course.getTitle());
                }

                return convertToEnrollmentGetDTO(savedEnrollment);
        }

        @Transactional(readOnly = true)
        public List<EnrollmentGetDTO> getAllEnrollments() {
                return enrollmentRepository.findAll().stream()
                                .map(this::convertToEnrollmentGetDTO)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public EnrollmentGetDTO getEnrollmentById(Long id) {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with ID: " + id));
                return convertToEnrollmentGetDTO(enrollment);
        }

        @Transactional
        public EnrollmentGetDTO updateEnrollment(Long id, EnrollmentPutDTO enrollmentPutDTO)
                        throws FirebaseMessagingException {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with ID: " + id));

                User student = userRepository.findByIdAndRole(enrollmentPutDTO.getStudentId(), "STUDENT")
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Student not found with ID: " + enrollmentPutDTO.getStudentId()));

                Course course = courseRepository.findById(enrollmentPutDTO.getCourseId())
                                .orElseThrow(() -> new CourseNotFoundException(
                                                "Course not found with ID: " + enrollmentPutDTO.getCourseId()));

                enrollment.setStudent(student);
                enrollment.setCourse(course);

                Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

                // Send notification
                if (student.getTokens() != null) {
                        notificationService.sendNotification(student.getTokens(), "Enrollment Updated",
                                        "Your enrollment in " + course.getTitle() + " has been updated.");
                }
                return convertToEnrollmentGetDTO(updatedEnrollment);
        }

        @Transactional
        public void deleteEnrollment(Long id) throws FirebaseMessagingException {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with ID: " + id));
                User student = enrollment.getStudent();
                Course course = enrollment.getCourse();
                enrollmentRepository.deleteById(id);

                // Send notification
                if (student.getTokens() != null) {
                        notificationService.sendNotification(student.getTokens(), "Enrollment Deleted",
                                        "Your enrollment in " + course.getTitle() + " has been deleted.");
                }
        }
}