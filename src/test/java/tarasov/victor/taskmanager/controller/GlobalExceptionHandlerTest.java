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
import tarasov.victor.taskmanager.repository.TaskRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tarasov.victor.taskmanager.constant.TestConstants.*;
import static tarasov.victor.taskmanager.model.Status.PENDING;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void testHandleTaskManagerException() throws Exception {
        var id = 0L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("task by id: " + id + " was not found!"));
    }

    @Test
    void testHandleGlobalException() throws Exception {
        var id = 0L;
        when(taskRepository.findById(id)).thenThrow(RuntimeException.class);
        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testHandleMethodArgumentNotValid() throws Exception {
        var task = new TaskDto(null, DESCRIPTION, PENDING);
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank"));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception{
        mockMvc.perform(delete(ENDPOINT + "/" + "a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("The parameter id of value 'a' could not be converted to type Long"));
    }
}