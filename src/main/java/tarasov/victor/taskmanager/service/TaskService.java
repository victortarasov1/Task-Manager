package tarasov.victor.taskmanager.service;

import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task findById(Long id);

    void add(TaskDto taskDto);

    void update(TaskDto taskDto, Long id);

    void delete(Long id);
}
