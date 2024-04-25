package tarasov.victor.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;

@Schema(description = "DTO for creating or updating a task")
public record TaskDto(
        @NotBlank(message = "Title cannot be blank")
        @Size(min = 1, max = 255, message = "Title length must be between 1 and 255 characters")
        @Schema(example = "Task 1")
        String title,

        @NotBlank(message = "Description cannot be blank")
        @Size(min = 1, max = 1000, message = "Description length must be between 1 and 1000 characters")
        @Schema(example = "This is a task description")
        String description,

        @NotNull(message = "Status cannot be null")
        @Schema(example = "PENDING")
        Status status
) {
    public Task createNewTask() {
        return new Task(title, description, status);
    }
}
