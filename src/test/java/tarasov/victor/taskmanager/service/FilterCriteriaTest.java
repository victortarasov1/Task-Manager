package tarasov.victor.taskmanager.service;

import org.junit.jupiter.api.Test;
import tarasov.victor.taskmanager.model.Task;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
import static tarasov.victor.taskmanager.model.Status.COMPLETED;
import static tarasov.victor.taskmanager.model.Status.IN_PROGRESS;

class FilterCriteriaTest {
    @Test
    void testIsMatchingWithTitle() {
        var criteria = new FilterCriteria(FIRST_TITLE, null, null);
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isTrue();
    }

    @Test
    void testIsMatchingWithDescription() {
        var criteria = new FilterCriteria(null, FIRST_DESCRIPTION, null);
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isTrue();
    }

    @Test
    void testIsMatchingWithStatus() {
        var criteria = new FilterCriteria(null, null, IN_PROGRESS);
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isTrue();
    }

    @Test
    void testIsMatchingWithAllParameters() {
        var criteria = new FilterCriteria(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isTrue();
    }

    @Test
    void testIsMatchingWithoutParameters() {
        var criteria = new FilterCriteria(null, null, null);
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isTrue();
    }

    @Test
    void testIsNotMatchingWithTitle() {
        var criteria = new FilterCriteria(FIRST_TITLE, null, null);
        var task = new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isFalse();
    }

    @Test
    void testIsNotMatchingWithDescription() {
        var criteria = new FilterCriteria(null, FIRST_DESCRIPTION, null);
        var task = new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isFalse();
    }

    @Test
    void testIsNotMatchingWithStatus() {
        var criteria = new FilterCriteria(null, null, COMPLETED);
        var task = new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS);

        var result = criteria.isMatching(task);

        assertThat(result).isFalse();
    }

}