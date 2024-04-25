package tarasov.victor.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.exception.TaskWasNotFoundByIdException;
import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static tarasov.victor.taskmanager.model.Status.*;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
class TaskServiceImplTest {
    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp(){
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void testFindAll() {
        var expected = List.of(new Task(), new Task());

        when(taskRepository.findAll()).thenReturn(expected);

        var result = taskService.findAll();

        assertSame(expected, result);
    }

    @Test
    void testFindById() {
        var id = 0L;
        var expected = new Task();
        expected.setId(id);

        when(taskRepository.findById(id)).thenReturn(Optional.of(expected));

        var result = taskService.findById(id);

        assertSame(expected.getId(), result.getId());
    }

    @Test
    void testFindById_shouldThrowTaskWasNotFoundException() {
        var id = 0L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(id)).isInstanceOf(TaskWasNotFoundByIdException.class);
    }

    @Test
    void testCreate() {
        var task = new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        taskService.create(task);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdate() {
        var id = 0L;
        var old = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);
        old.setId(id);
        var newTask = new TaskDto(SECOND_TITLE, SECOND_DESCRIPTION, COMPLETED);

        when(taskRepository.findById(id)).thenReturn(Optional.of(old));

        taskService.update(newTask, id);

        assertSame(old.getDescription(),SECOND_DESCRIPTION);
        assertSame(old.getStatus(), Status.COMPLETED);
        assertSame(old.getTitle(), SECOND_TITLE);

    }

    @Test
    void testUpdate_shouldThrownTaskWasNotFoundByIdException() {
        var id = 0L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), id)).isInstanceOf(TaskWasNotFoundByIdException.class);
    }

    @Test
    void testDelete() {
        var id = 0L;

        when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));

        taskService.delete(id);

        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void testDelete_shouldThrownTaskWasNotFoundByIdException() {
        var id = 0L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(id)).isInstanceOf(TaskWasNotFoundByIdException.class);
    }


}