package id.backend.session_2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.backend.session_2.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t WHERE t.user.id = :user_id")
    List<Task> findTaskByUser(@Param("user_id") int user_id);

    @Query("SELECT t FROM Task t ORDER BY t.title ASC")
    List<Task> findAllTaskSortedByName();

    @Query("SELECT t.priority, COUNT(t) FROM Task t GROUP BY t.priority")
    List<Object[]> countTaskByPriority();
}
