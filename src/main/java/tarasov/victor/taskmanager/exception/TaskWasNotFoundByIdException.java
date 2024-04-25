package tarasov.victor.taskmanager.exception;

public class TaskWasNotFoundByIdException extends TaskManagerException {
    public TaskWasNotFoundByIdException(Long id) {
        super("task by id: " + id + " was not found!");
    }
}
