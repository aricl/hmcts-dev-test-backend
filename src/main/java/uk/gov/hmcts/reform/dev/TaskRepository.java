package uk.gov.hmcts.reform.dev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.gov.hmcts.reform.dev.models.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM TASKS WHERE id = ?1", nativeQuery = true)
    List<Task> getTasksByTitle(String title);
}
