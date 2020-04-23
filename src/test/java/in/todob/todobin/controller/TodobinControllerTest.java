package in.todob.todobin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.todob.todobin.exception.ErrorInfo;
import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodobinService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class TodobinControllerTest {

    @MockBean
    private TodobinService mockTodobinService;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper om;

    @Before
    public void setUp() throws Exception {
        om = new ObjectMapper();
    }

    @Test
    public void createTodo_returns201_whenTodoPersistedSuccessfully() throws Exception {
        Todo todo = Todo.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .build();

        when(mockTodobinService.createTodo(any(Todo.class))).thenReturn(todo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/todo")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(om.writeValueAsString(todo)))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        Todo actual = om.readValue(result.getResponse().getContentAsString(), Todo.class);

        assertThat(result.getResponse().getHeader("Location")).isEqualTo("http://localhost/todo/1");
        assertThat(actual).isEqualTo(todo);
    }

    @Test
    public void getTodos_returns200withListofTodos() throws Exception {
        List<Todo> todos = Arrays.asList(
                Todo.builder()
                    .id(1L)
                    .title("Todo 1")
                    .description("A description")
                    .build(),
                Todo.builder()
                    .id(2L)
                    .title("Todo 2")
                    .description("A description")
                    .build()
        );

        when(mockTodobinService.getTodos()).thenReturn(todos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        List<Todo> actual = om.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Todo>>() {
        });

        assertThat(actual).isEqualTo(todos);
    }

    @Test
    public void getTodo_returns200WithTodo_whenPassedExistingTodoId() throws Exception {
        Todo todo = Todo.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .build();

        when(mockTodobinService.getTodo(1L)).thenReturn(todo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/1"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        Todo actual = om.readValue(result.getResponse().getContentAsString(), Todo.class);

        assertThat(actual).isEqualTo(todo);
    }

    @Test
    public void getTodo_returns404_whenPassedNonexistentTodoId() throws Exception {
        when(mockTodobinService.getTodo(1)).thenThrow(new TodoNotFoundException(1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/1"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todo with ID '1' not found.");
    }

    @Test
    public void deleteTodo_returns200_whenSuccessfullyDeleteAValidTodo() throws Exception {

        when(mockTodobinService.getTodo(1L)).thenReturn(Todo.builder().build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/todo/1"))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteTodo_returns404_whenPassedNonExistentTodoIdForDeletion() throws Exception {
        when(mockTodobinService.getTodo(1)).thenThrow(new TodoNotFoundException(1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/1"))
                                  .andExpect(status().isNotFound())
                                  .andReturn();

        ErrorInfo error = om.readValue(result.getResponse().getContentAsString(), ErrorInfo.class);

        assertThat(error.getMessage()).isEqualTo("Todo with ID '1' not found.");
    }
}
