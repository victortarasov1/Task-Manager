package tarasov.victor.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.service.TaskService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
import static tarasov.victor.taskmanager.model.Status.COMPLETED;
import static tarasov.victor.taskmanager.model.Status.IN_PROGRESS;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TaskService taskService;

    @Test
    void testFindAll() throws Exception {
        var tasks = List.of(new Task(), new Task());
        when(taskService.findAll()).thenReturn(tasks);
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void testFindById() throws Exception {
        var id = 0L;
        var task = new Task(TITLE, DESCRIPTION, IN_PROGRESS);
        task.setId(id);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.status").value(IN_PROGRESS.toString()));
    }

    @Test
    void testAdd() throws Exception {
        var task = new TaskDto(TITLE, DESCRIPTION, IN_PROGRESS);
        mockMvc.perform(post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        var id = 0L;
        var task = new Task(TITLE, DESCRIPTION, IN_PROGRESS);
        var taskDto = new TaskDto(UPDATED_TITLE, UPDATED_DESCRIPTION, COMPLETED);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(put(ENDPOINT + "/" + id)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        var id = 0L;
        var task = new Task(TITLE, DESCRIPTION, IN_PROGRESS);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(delete(ENDPOINT + "/" + id))
                .andExpect(status().isOk());
    }
}