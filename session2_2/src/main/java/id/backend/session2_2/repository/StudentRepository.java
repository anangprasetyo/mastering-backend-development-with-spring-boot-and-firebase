package id.backend.session2_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.backend.session2_2.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
