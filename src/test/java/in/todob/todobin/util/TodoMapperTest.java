package in.todob.todobin.util;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class TodoMapperTest {

    private Todo todo1;
    private Todo todo2;
    private List<Todo> todos;

    private TodoMapper mapper;

    @Before
    public void setUp() throws Exception {
        todo1 = Todo.builder().id(1L).shortId("U").title("A title").notes("None").build();
        todo2 = Todo.builder().id(2L).shortId("B").title("A second title").notes("None either").build();

        todos = Arrays.asList(todo1, todo2);

        mapper = Mappers.getMapper(TodoMapper.class);
    }

    @Test
    public void mapToTodoResponse_returnTodoResponse_whenPassedTodo() {
        TodoResponse actual = mapper.mapTodoToTodoResponse(todo1);

        assertThat(actual.getShortId()).isEqualTo("u");
        assertThat(actual.getTitle()).isEqualTo("A title");
        assertThat(actual.getNotes()).isEqualTo("None");
    }

    @Test
    public void mapToTodoResponseList_returnsListOfTodoResponse_whenPassedListofTodo() {
        List<TodoResponse> actual = mapper.mapTodoListToTodoResponseList(todos);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getShortId()).isEqualTo("u");
        assertThat(actual.get(0).getTitle()).isEqualTo("A title");
        assertThat(actual.get(0).getNotes()).isEqualTo("None");
        assertThat(actual.get(1).getShortId()).isEqualTo("B");
        assertThat(actual.get(1).getTitle()).isEqualTo("A second title");
        assertThat(actual.get(1).getNotes()).isEqualTo("None either");
    }

    @Test
    public void mapToTodo_returnsTodo_whenPassedTodoRequest() {
        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A title");
        todoRequest.setNotes("None");

        Todo actual = mapper.mapTodoRequestToTodo(todoRequest);

        assertThat(actual.getTitle()).isEqualTo("A title");
        assertThat(actual.getNotes()).isEqualTo("None");
    }
}