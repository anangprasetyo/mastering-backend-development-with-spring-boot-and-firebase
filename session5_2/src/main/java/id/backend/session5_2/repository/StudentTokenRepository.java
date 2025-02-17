package id.backend.session5_2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.backend.session5_2.model.StudentToken;

@Repository
public interface StudentTokenRepository extends JpaRepository<StudentToken, Integer> {

    @Query("SELECT st FROM StudentToken st WHERE st.student.id = :student_id")
    Optional<StudentToken> findByStudentId(@Param("student_id") int student_id);

}
