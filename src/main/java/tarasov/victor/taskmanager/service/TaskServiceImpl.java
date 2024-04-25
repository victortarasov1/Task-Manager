package tarasov.victor.taskmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.exception.TaskWasNotFoundByIdException;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskWasNotFoundByIdException(id));
    }

    @Override
    public void add(TaskDto taskDto) {
        taskRepository.save(taskDto.createNewTask());
    }

    @Override
    public void update(TaskDto taskDto, Long id) {
        var updatingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskWasNotFoundByIdException(id));
        updatingTask.setStatus(taskDto.status());
        updatingTask.setTitle(taskDto.title());
        updatingTask.setDescription(taskDto.description());
        taskRepository.save(updatingTask);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var deletingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskWasNotFoundByIdException(id));
        taskRepository.delete(deletingTask);
    }
}
