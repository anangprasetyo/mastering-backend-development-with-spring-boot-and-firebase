package id.project.education.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.firebase.messaging.FirebaseMessagingException;

import id.project.education.dto.enrollment.EnrollmentGetDTO;
import id.project.education.dto.enrollment.EnrollmentPostDTO;
import id.project.education.dto.enrollment.EnrollmentPutDTO;
import id.project.education.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentGetDTO> createEnrollment(@RequestBody EnrollmentPostDTO enrollmentPostDTO)
            throws FirebaseMessagingException {
        EnrollmentGetDTO enrollment = enrollmentService.createEnrollment(enrollmentPostDTO);
        return ResponseEntity.ok(enrollment);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentGetDTO>> getAllEnrollments() {
        List<EnrollmentGetDTO> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentGetDTO> getEnrollmentById(@PathVariable Long id) {
        EnrollmentGetDTO enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentGetDTO> updateEnrollment(@PathVariable Long id,
            @RequestBody EnrollmentPutDTO enrollmentPutDTO) throws FirebaseMessagingException {
        EnrollmentGetDTO enrollment = enrollmentService.updateEnrollment(id, enrollmentPutDTO);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) throws FirebaseMessagingException {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}