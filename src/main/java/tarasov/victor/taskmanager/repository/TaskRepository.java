package tarasov.victor.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tarasov.victor.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
