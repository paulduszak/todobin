package in.todob.todobin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
}
