package id.project.education.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.project.education.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);
}
