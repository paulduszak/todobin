package in.todob.todobin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodolistRequest;
import in.todob.todobin.dto.TodolistResponse;
import in.todob.todobin.exception.ErrorInfo;
import in.todob.todobin.exception.RestExceptionHandler;
import in.todob.todobin.exception.TodolistNotFoundException;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.service.TodolistService;
import in.todob.todobin.util.TodolistMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TodolistControllerTest {

    @Mock
    private TodolistService mockTodolistService;

    @Mock
    private TodolistMapper mockTodolistMapper;

    @InjectMocks
    private TodolistController todolistController;

    private MockMvc mockMvc;

    private static ObjectMapper om;

    @Before
    public void setUp() throws Exception {
        om = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(todolistController)
                                 .setControllerAdvice(new RestExceptionHandler())
                                 .build();
    }

    @Test
    public void createTodolist_returns201_whenTodoPersistedSuccessfully() throws Exception {
        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A todo");
        todoRequest.setNotes("A todo description");

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTitle("A todo list");
        todolistRequest.setNotes("A todo list description");
        todolistRequest.setTodos(asList(todoRequest));

        when(mockTodolistService.createTodolist(any(TodolistRequest.class))).thenReturn(Todolist.builder().build());
        when(mockTodolistMapper.mapTodolistToTodolistResponse(any(Todolist.class))).thenReturn(new TodolistResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/list")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(om.writeValueAsString(todolistRequest)))
               .andExpect(status().isCreated())
               .andReturn();
    }

    @Test
    public void patchTodolist_returns200_whenTodolistPatchedSuccessfully() throws Exception {

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTodos(asList(new TodoRequest()));

        when(mockTodolistService.patchTodolist(eq("2"), any(TodolistRequest.class))).thenReturn(Todolist.builder().build());
        when(mockTodolistMapper.mapTodolistToTodolistResponse(any(Todolist.class))).thenReturn(new TodolistResponse());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/list/2")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(om.writeValueAsString(todolistRequest)))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    public void getTodolists_returns200_withListOfTodolists() throws Exception {

        when(mockTodolistService.getTodolists()).thenReturn(asList(Todolist.builder().build(), Todolist.builder().build()));
        when(mockTodolistMapper.mapTodolistListToTodolistResponseList(any(List.class))).thenReturn(asList(new TodolistResponse(), new TodolistResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    @WithUserDetails(value = "userA")
    public void getTodolist_returns200_withTodolist() throws Exception {

//        when(mockTodolistService.getTodolist("u")).thenReturn((Todolist.builder().build()));
//        when(mockTodolistMapper.mapTodolistToTodolistResponse(any(Todolist.class))).thenReturn(new TodolistResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    public void getTodolist_returns404_whenPassedNonexistentTodolistId() throws Exception {
        when(mockTodolistService.getTodolist("j4")).thenThrow(new TodolistNotFoundException("j4"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list/j4"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todolist with ID 'j4' not found.");
    }

    @Test
    public void deleteTodolist_returns200_whenSuccessfullyDeleteAValidTodolist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/list/j4"))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteTodo_returns404_whenPassedNonExistentTodolistIdForDeletion() throws Exception {
        when(mockTodolistService.getTodolist("k4")).thenThrow(new TodolistNotFoundException("k4"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list/k4"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todolist with ID 'k4' not found.");
    }
}