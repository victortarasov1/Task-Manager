package tarasov.victor.taskmanager.service;

import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;

import java.util.List;

public interface FilteringService {
    List<Task> filter(List<Task> tasks, String title, String description, Status status);
}
