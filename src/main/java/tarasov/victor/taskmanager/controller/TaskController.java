package tarasov.victor.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.service.FilterCriteria;
import tarasov.victor.taskmanager.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Retrieve tasks", description = "Retrieves a list of tasks optionally filtered by title, description, and status.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved tasks")})
    public List<Task> findAll(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) Status status) {
        var filterCriteria = new FilterCriteria(title, description, status);
        return taskService.findAll().stream().filter(filterCriteria::isMatching).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find task by ID", description = "Retrieves a task by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "409", description = "Task not found by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request, e.g., ID type mismatch")
    })
    public Task findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task", description = "Creates a new task based on the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public void add(@RequestBody @Valid TaskDto dto) {
        taskService.add(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task by ID", description = "Updates an existing task with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "409", description = "Task not found by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public void update(@PathVariable Long id, @RequestBody @Valid TaskDto dto) {
        taskService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by ID", description = "Deletes a task by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "409", description = "Task not found by ID"),
            @ApiResponse(responseCode = "400", description = "Invalid request, e.g., ID type mismatch")
    })
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }


}
