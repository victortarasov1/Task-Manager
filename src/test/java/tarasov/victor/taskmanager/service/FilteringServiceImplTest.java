package tarasov.victor.taskmanager.service;

import org.junit.jupiter.api.Test;
import tarasov.victor.taskmanager.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static tarasov.victor.taskmanager.model.Status.*;

class FilteringServiceImplTest {

    private final FilteringService filteringService = new FilteringServiceImpl();

    @Test
    void testFilter_whenNoFilteringParametersProvided() {
        var tasks = List.of(new Task(), new Task());
        var result = filteringService.filter(tasks, null, null, null);
        assertEquals(tasks.size(), result.size());
    }

    @Test
    void testFilter_whenOnlyTitleParamProvided() {
        var first = new Task("title 1", "description 1", IN_PROGRESS);
        first.setId(0L);
        var second = new Task("title 2", "description 2", COMPLETED);
        second.setId(1L);
        var tasks = List.of(first, second);
        var result = filteringService.filter(tasks, "1", null, null);
        assertEquals(1, result.size());
        assertSame(tasks.get(0).getId(), result.get(0).getId());
    }

    @Test
    void testFilter_whenOnlyDescriptionParamProvided() {
        var first = new Task("title 1", "description 1", IN_PROGRESS);
        first.setId(0L);
        var second = new Task("title 2", "description 2", COMPLETED);
        second.setId(1L);
        var tasks = List.of(first, second);
        var result = filteringService.filter(tasks, null, "1", null);
        assertEquals(1, result.size());
        assertSame(tasks.get(0).getId(), result.get(0).getId());
    }

    @Test
    void testFilter_whenOnlyStatusParamProvided() {
        var first = new Task("title 1", "description 1", PENDING);
        first.setId(0L);
        var second = new Task("title 2", "description 2", COMPLETED);
        second.setId(1L);
        var tasks = List.of(first, second);
        var result = filteringService.filter(tasks, null, null, PENDING);
        assertEquals(1, result.size());
        assertSame(tasks.get(0).getId(), result.get(0).getId());
    }

    @Test
    void testFilter_whenAllParamsProvided() {
        var first = new Task("title 1", "description 1", IN_PROGRESS);
        first.setId(0L);
        var second = new Task("title 2", "description 2", COMPLETED);
        second.setId(1L);
        var third = new Task("title 12", "description 3", IN_PROGRESS);
        third.setId(2L);
        var tasks = List.of(first, second, third);
        var result = filteringService.filter(tasks, "1", "descr", IN_PROGRESS);

        assertEquals(2, result.size());
        assertEquals(first.getId(), result.get(0).getId());
        assertEquals(third.getId(), result.get(1).getId());

    }
}