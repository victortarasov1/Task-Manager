package tarasov.victor.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tarasov.victor.taskmanager.dto.TaskDto;
import tarasov.victor.taskmanager.model.Task;
import tarasov.victor.taskmanager.service.TaskService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
import static tarasov.victor.taskmanager.model.Status.IN_PROGRESS;

@WebMvcTest
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
    void testFindAll_WithNoParameters() throws Exception {
        var tasks = List.of(new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS));

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }" +
                        ",{ \"title\": \"title 2\", \"description\": \"description 2\", \"status\": \"IN_PROGRESS\" }]"));
        verify(taskService, times(1)).findAll();

    }

    @Test
    void testFindAll_withTitleParameter() throws Exception {
        var tasks = List.of(new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS));

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get(ENDPOINT).param(TITLE_PARAM, FIRST_TITLE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));

        verify(taskService, times(1)).findAll();
    }

    @Test
    void testFindAll_withDescriptionParameter() throws Exception {
        var tasks = List.of(new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS));

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get(ENDPOINT).param(DESCRIPTION_PARAM, FIRST_DESCRIPTION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));

        verify(taskService, times(1)).findAll();
    }


    @Test
    void testFindAll_withStatusParameter() throws Exception {
        var tasks = List.of(new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS));

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get(ENDPOINT).param(STATUS_PARAM, IN_PROGRESS.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }" +
                        ",{ \"title\": \"title 2\", \"description\": \"description 2\", \"status\": \"IN_PROGRESS\" }]"));

        verify(taskService, times(1)).findAll();
    }

    @Test
    void testFindAll_withAllParameters() throws Exception {
        var tasks = List.of(new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS), new Task(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS));

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get(ENDPOINT)
                        .param(STATUS_PARAM, IN_PROGRESS.toString())
                        .param(TITLE_PARAM, FIRST_TITLE)
                        .param(DESCRIPTION_PARAM, FIRST_DESCRIPTION)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json("[{ \"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }]"));

        verify(taskService, times(1)).findAll();
    }
    @Test
    void testFindById() throws Exception {
        var id = 0L;
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);
        task.setId(id);

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"id\": 0 ,\"title\": \"title 1\", \"description\": \"description 1\", \"status\": \"IN_PROGRESS\" }"));
        verify(taskService, times(1)).findById(id);
    }

    @Test
    void testCreate() throws Exception {
        var task = new TaskDto(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isCreated());
        verify(taskService, times(1)).create(eq(task));
    }

    @Test
    void testUpdate() throws Exception {
        var id = 0L;
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);
        var taskDto = new TaskDto(SECOND_TITLE, SECOND_DESCRIPTION, IN_PROGRESS);

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(put(ENDPOINT + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());
        verify(taskService, times(1)).update(eq(taskDto), eq(id));
    }

    @Test
    void testDelete() throws Exception {
        var id = 0L;
        var task = new Task(FIRST_TITLE, FIRST_DESCRIPTION, IN_PROGRESS);

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(delete(ENDPOINT + "/" + id))
                .andExpect(status().isOk());
        verify(taskService, times(1)).delete(id);
    }
}