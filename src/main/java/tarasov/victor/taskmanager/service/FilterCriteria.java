package tarasov.victor.taskmanager.service;

import tarasov.victor.taskmanager.model.Status;
import tarasov.victor.taskmanager.model.Task;

public class FilterCriteria {
    private final String title;
    private final String description;
    private final String status;

    public FilterCriteria(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status != null ? status.toString() : null;
    }

    public boolean isMatching(Task task) {
        boolean titleMatches = containsIfSpecified(task.getTitle(), title);
        boolean descriptionMatches = containsIfSpecified(task.getDescription(), description);
        boolean statusMatches = containsIfSpecified(task.getStatus().toString(), status);
        return titleMatches && descriptionMatches && statusMatches;
    }

    private boolean containsIfSpecified(String value, String filterValue) {
        return filterValue == null || filterValue.isEmpty() || value.contains(filterValue);
    }
}
