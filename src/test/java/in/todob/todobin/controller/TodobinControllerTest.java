package in.todob.todobin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.exception.ErrorInfo;
import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodobinService;
import in.todob.todobin.service.TodolistService;
import in.todob.todobin.util.TodoMapper;
import in.todob.todobin.util.TodolistMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class TodobinControllerTest {

    @MockBean
    private TodobinService mockTodobinService;

    @MockBean
    private TodoMapper mockTodoMapper;

    @MockBean
    private TodolistService mockTodolistService;

    @MockBean
    private TodolistMapper mockTodolistMapper;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper om;

    @Before
    public void setUp() throws Exception {
        om = new ObjectMapper();
    }

    @Test
    public void createTodo_returns201_whenTodoPersistedSuccessfully() throws Exception {
        TodoRequest todoRequest = new TodoRequest();
            todoRequest.setTitle("A todo");
            todoRequest.setNotes("A todo description");

        Todo todo = Todo.builder()
                        .id(2L)
                        .title("A todo")
                        .notes("A todo description")
                        .build();

        TodoResponse todoResponse = new TodoResponse();
            todoResponse.setShortId("B");
            todoResponse.setTitle("A todo");
            todoResponse.setNotes("A todo description");
            todoResponse.setStatus(false);

        when(mockTodobinService.createTodo(eq("u"), any(TodoRequest.class))).thenReturn(todo);

        when(mockTodoMapper.mapTodoToTodoResponse(eq(todo))).thenReturn(todoResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/list/u/todo")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(om.writeValueAsString(todoRequest)))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        TodoResponse actual = om.readValue(result.getResponse().getContentAsString(), TodoResponse.class);

        assertThat(result.getResponse().getHeader("Location")).isEqualTo("http://localhost/api/v1/list/u/todo/B");
        assertThat(actual).isEqualTo(todoResponse);
    }

    @Test
    public void patchTodo_returns200_whenTodoPatchedSuccessfully() throws Exception {
        TodoRequest todoRequest = new TodoRequest();
            todoRequest.setTitle("Updated Title");
            todoRequest.setNotes("Updated Description");

        Todo todo = Todo.builder()
                         .id(2L)
                         .title("Updated Title")
                         .notes("Updated Description")
                         .build();

        TodoResponse todoResponse = new TodoResponse();
            todoResponse.setShortId("B");
            todoResponse.setTitle("Updated Title");
            todoResponse.setNotes("Updated Description");
            todoResponse.setStatus(true);

        when(mockTodobinService.patchTodo(eq("u"), eq("B"), any(TodoRequest.class))).thenReturn(todo);
        when(mockTodoMapper.mapTodoToTodoResponse(eq(todo))).thenReturn(todoResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/list/u/todo/B")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(om.writeValueAsString(todoRequest)))
                                  .andExpect(status().isOk())
                                  .andReturn();

        TodoResponse actual = om.readValue(result.getResponse().getContentAsString(), TodoResponse.class);

        assertThat(actual).isEqualTo(todoResponse);
    }

    @Test
    public void getTodo_returns200WithTodo_whenPassedExistingTodoId() throws Exception {
        Todo todo = Todo.builder()
                        .shortId("2c")
                        .title("Title")
                        .notes("Description")
                        .status(true)
                        .build();

        TodoResponse todoResponse = new TodoResponse();
            todoResponse.setShortId("2c");
            todoResponse.setTitle("Title");
            todoResponse.setNotes("Description");
            todoResponse.setStatus(true);

        TodoResponse expectedTodoResponse = new TodoResponse();
            expectedTodoResponse.setShortId("2c");
            expectedTodoResponse.setTitle("Title");
            expectedTodoResponse.setNotes("Description");
            expectedTodoResponse.setStatus(true);

        when(mockTodobinService.getTodo("u", "2c")).thenReturn(todo);
        when(mockTodoMapper.mapTodoToTodoResponse(eq(todo))).thenReturn(todoResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list/u/todo/2c"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        TodoResponse actual = om.readValue(result.getResponse().getContentAsString(), TodoResponse.class);

        assertThat(actual).isEqualTo(expectedTodoResponse);
    }

    @Test
    public void getTodo_returns404_whenPassedNonexistentTodoId() throws Exception {
        when(mockTodobinService.getTodo("u", "j4")).thenThrow(new TodoNotFoundException("j4"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list/u/todo/j4"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todo with ID 'j4' not found.");
    }

    @Test
    public void deleteTodo_returns200_whenSuccessfullyDeleteAValidTodo() throws Exception {

        when(mockTodobinService.getTodo("u", "j4")).thenReturn(Todo.builder().build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/list/u/todo/j4"))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteTodo_returns404_whenPassedNonExistentTodoIdForDeletion() throws Exception {
        when(mockTodobinService.getTodo("u", "k4")).thenThrow(new TodoNotFoundException("k4"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list/u/todo/k4"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todo with ID 'k4' not found.");
    }
}
