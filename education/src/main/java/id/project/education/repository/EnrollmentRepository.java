package id.project.education.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.project.education.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
}