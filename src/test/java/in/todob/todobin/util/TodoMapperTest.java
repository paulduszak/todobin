package in.todob.todobin.util;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class TodoMapperTest {

    private Todo todo1;
    private Todo todo2;
    private List<Todo> todos;

    @Before
    public void setUp() throws Exception {
        todo1 = Todo.builder().id(1L).shortId("U").title("A title").description("None").build();
        todo2 = Todo.builder().id(2L).shortId("B").title("A second title").description("None either").build();

        todos = Arrays.asList(todo1, todo2);
    }

    @Test
    public void mapToTodoResponse_returnTodoResponse_whenPassedTodo() {
        TodoResponse actual = TodoMapper.mapToTodoResponse(todo1);

        assertThat(actual.getShortId()).isEqualTo("u");
        assertThat(actual.getTitle()).isEqualTo("A title");
        assertThat(actual.getDescription()).isEqualTo("None");
    }

    @Test
    public void mapToTodoResponseList_returnsListOfTodoResponse_whenPassedListofTodo() {
        List<TodoResponse> actual = TodoMapper.mapToTodoResponseList(todos);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getShortId()).isEqualTo("u");
        assertThat(actual.get(0).getTitle()).isEqualTo("A title");
        assertThat(actual.get(0).getDescription()).isEqualTo("None");
        assertThat(actual.get(1).getShortId()).isEqualTo("B");
        assertThat(actual.get(1).getTitle()).isEqualTo("A second title");
        assertThat(actual.get(1).getDescription()).isEqualTo("None either");
    }

    @Test
    public void mapToTodo_returnsTodo_whenPassedTodoRequest() {
        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A title");
        todoRequest.setDescription("None");

        Todo actual = TodoMapper.mapToTodo(todoRequest);

        assertThat(actual.getTitle()).isEqualTo("A title");
        assertThat(actual.getDescription()).isEqualTo("None");
    }
}