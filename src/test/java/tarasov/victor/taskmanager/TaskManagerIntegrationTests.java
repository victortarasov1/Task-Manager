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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
        var first = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);
        var second = new Task(SECOND_TITLE, SECOND_DESCRIPTION, COMPLETED);
        var third = new Task(THIRD_TITLE, THIRD_DESCRIPTION, IN_PROGRESS);

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
        mockMvc.perform(get(ENDPOINT).param(TITLE_PARAM, FIRST_TITLE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));
    }

    @Test
    void testFindAll_withDescriptionParameter() throws Exception {
        mockMvc.perform(get(ENDPOINT).param(DESCRIPTION_PARAM, FIRST_DESCRIPTION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));
    }

    @Test
    void testFindAll_withStatusParameter() throws Exception {
        mockMvc.perform(get(ENDPOINT).param(STATUS_PARAM, IN_PROGRESS.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }" +
                        ",{ \"title\": \"title 3\", \"description\": \"description 3\", \"status\": \"IN_PROGRESS\" }]"));
    }

    @Test
    void testFindAll_withAllParameters() throws Exception {
        mockMvc.perform(get(ENDPOINT)
                        .param(STATUS_PARAM, IN_PROGRESS.toString())
                        .param(TITLE_PARAM, FIRST_TITLE)
                        .param(DESCRIPTION_PARAM, FIRST_DESCRIPTION)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));
    }

    @Test
    void testFindById() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();

        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"title\": \"title 3\", \"description\": \"description 3\", \"status\": \"IN_PROGRESS\" }"));
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
    void testCreate() throws Exception {
        var task = new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreate_shouldHandleMethodArgumentNotValid() throws Exception {
        var task = new TaskDto(null, FIRST_DESCRIPTION, IN_PROGRESS);

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
        var task = new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_shouldHandleMethodArgumentNotValid() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId();
        var task = new TaskDto(null, FIRST_DESCRIPTION, IN_PROGRESS);

        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank"));
    }


    @Test
    void testUpdate_shouldHandleTaskManagerException() throws Exception {
        var tasks = taskRepository.findAll();
        var id = tasks.get(tasks.size() - 1).getId() + 1;
        var task = new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

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


}
