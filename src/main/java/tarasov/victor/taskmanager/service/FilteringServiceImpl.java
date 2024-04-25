package tarasov.victor.taskmanager.service;

import org.springframework.stereotype.Service;
import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;

import java.util.List;
import java.util.function.Predicate;

@Service
public class FilteringServiceImpl implements FilteringService {
    @Override
    public List<Task> filter(List<Task> tasks, String title, String description, Status status) {
        Predicate<Task> byTitle = t -> title == null || t.getTitle().contains(title);
        Predicate<Task> byDescription = t -> description == null || t.getDescription().contains(description);
        Predicate<Task> byStatus = t -> status == null || t.getStatus().equals(status);
        return tasks.stream().filter(byTitle).filter(byStatus).filter(byDescription).toList();
    }
}
