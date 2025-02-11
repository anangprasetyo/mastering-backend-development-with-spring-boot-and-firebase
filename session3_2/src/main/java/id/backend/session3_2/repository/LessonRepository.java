package id.backend.session3_2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.backend.session3_2.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query("SELECT l FROM Lesson l WHERE l.student.id = :student_id")
    List<Lesson> findLessonByStudent(@Param("student_id") int student_id);

}
