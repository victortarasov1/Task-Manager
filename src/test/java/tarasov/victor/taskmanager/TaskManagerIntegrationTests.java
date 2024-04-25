package tarasov.victor.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.repository.TaskRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
import static tarasov.victor.taskmanager.model.Status.COMPLETED;
import static tarasov.victor.taskmanager.model.Status.IN_PROGRESS;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TaskManagerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        var first = new Task("title 1", "description 1", IN_PROGRESS);
        var second = new Task("title 2", "description 2", COMPLETED);
        var third = new Task("title 3", "description 3", IN_PROGRESS);
        taskRepository.saveAll(List.of(first, second, third));
    }

    @AfterEach
    void clear() {
        taskRepository.deleteAll();
    }

    @Test
    void testFindAll_withNoParameters() throws Exception {
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testFindAll_withTitleParameter() throws Exception {
        mockMvc.perform(get(ENDPOINT).param("title", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].description").value("description 1"))
                .andExpect(jsonPath("$[0].status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testFindAll_withDescriptionParameter() throws Exception {
        mockMvc.perform(get(ENDPOINT).param("description", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].description").value("description 1"))
                .andExpect(jsonPath("$[0].status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testFindAll_withStatusParameter() throws Exception {
        mockMvc.perform(get(ENDPOINT).param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].description").value("description 1"))
                .andExpect(jsonPath("$[0].status").value(IN_PROGRESS.toString()))
                .andExpect(jsonPath("$[1].title").value("title 3"))
                .andExpect(jsonPath("$[1].description").value("description 3"))
                .andExpect(jsonPath("$[1].status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testFindAll_withAllParameters() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param("status", "IN_PROGRESS")
                        .param("title", "title 1")
                        .param("description", "description 1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[0].description").value("description 1"))
                .andExpect(jsonPath("$[0].status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testFindById() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();
        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title 3"))
                .andExpect(jsonPath("$.description").value("description 3"))
                .andExpect(jsonPath("$.status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testFindById_shouldHandleTaskManagerException() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId() + 1;
        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("task by id: " + id + " was not found!"));
    }

    @Test
    void testFindById_shouldHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get(ENDPOINT + "/a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("The parameter id of value 'a' could not be converted to type Long"));
    }

    @Test
    void testAdd() throws Exception {
        var task = new TaskDto("title 4", "description 4", IN_PROGRESS);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAdd_shouldHandleMethodArgumentNotValid() throws Exception {
        var task = new TaskDto(null, "description 4", IN_PROGRESS);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank"));
    }

    @Test
    void testUpdate() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();
        var task = new TaskDto("title 4", "description 4", IN_PROGRESS);
        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_shouldHandleMethodArgumentNotValid() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();
        var task = new TaskDto(null, "description 4", IN_PROGRESS);
        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank"));
    }

    @Test
    void testUpdate_shouldHandleMethodArgumentTypeMismatchException() throws Exception {
        var task = new TaskDto(null, "description 4", IN_PROGRESS);
        mockMvc.perform(put(ENDPOINT + "/a")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("The parameter id of value 'a' could not be converted to type Long"));
    }

    @Test
    void testUpdate_shouldHandleTaskManagerException() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId() + 1;
        var task = new TaskDto("title 4", "description 4", IN_PROGRESS);
        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("task by id: " + id + " was not found!"));
    }

    @Test
    void testDelete() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();
        mockMvc.perform(delete(ENDPOINT + "/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_shouldHandleTaskManagerException() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId() + 1;
        mockMvc.perform(delete(ENDPOINT + "/" + id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("task by id: " + id + " was not found!"));
    }

    @Test
    void testDelete_shouldHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(delete(ENDPOINT + "/a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("The parameter id of value 'a' could not be converted to type Long"));
    }

}
