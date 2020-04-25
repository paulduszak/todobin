package in.todob.todobin.service;

import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodobinServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private TodobinRepository mockTodobinRepository;

    private TodobinService todobinService;

    @Before
    public void setup() {
        todobinService = new TodobinService(mockTodobinRepository);
    }

    @Test
    public void createTodo_persistsTodo_whenPassedTodo() {
        Todo todo = Todo.builder()
                        .id(1L)
                        .title("A todo")
                        .description("A todo description")
                        .build();

        when(mockTodobinRepository.save(any(Todo.class))).thenReturn(todo);

        Todo result = todobinService.createTodo(todo);

        assertThat(result).isEqualToComparingFieldByField(todo);
        verify(mockTodobinRepository).save(todo);
    }

    @Test
    public void deleteTodo_deletesTodo_whenPassedValidTodoId() {
        when(mockTodobinRepository.findById(1L)).thenReturn(Optional.ofNullable(Todo.builder().build()));

        todobinService.deleteTodo(1L);

        verify(mockTodobinRepository).deleteById(1L);
    }

    @Test
    public void deleteTodo_throwsTodoNotFoundException_whenTodoIdForDeletionDoesNotExist() {
        thrown.expect(TodoNotFoundException.class);
        thrown.expectMessage("Todo with ID '1' not found.");

        when(mockTodobinRepository.findById(1L)).thenReturn(Optional.empty());

        todobinService.deleteTodo(1L);
    }

    @Test
    public void getTodos_retrievesPersistedTodos() {
        List<Todo> todos = Arrays.asList(
                Todo.builder()
                    .id(1L)
                    .title("Todo 1")
                    .description("A description")
                    .build(),
                Todo.builder()
                    .id(2L)
                    .title("Todo 1")
                    .description("A description")
                    .build()
        );

        when(mockTodobinRepository.findAll()).thenReturn(todos);

        List<Todo> result = todobinService.getTodos();

        assertThat(result).isEqualTo(todos);
    }

    @Test
    public void getTodo_returnsTodoIfExists() {
        Todo todo = Todo.builder()
                        .id(1L)
                        .title("A todo")
                        .description("A todo description")
                        .build();

        when(mockTodobinRepository.findById(1L)).thenReturn(Optional.ofNullable(todo));

        Todo result = todobinService.getTodo(1L);

        assertThat(result).isEqualTo(todo);
    }

    @Test
    public void patchTodo_patchesTodo_whenPassedFieldsWithUpdatedValues() {
        Todo existingTodo = Todo.builder()
                                .id(1L)
                                .title("A todo")
                                .description("A todo description")
                                .build();

        Todo updatedTodo = Todo.builder()
                               .title("An updated todo")
                               .description("An updated todo description")
                               .build();

        Todo expected = Todo.builder()
                            .id(1L)
                            .title("An updated todo")
                            .description("An updated todo description")
                            .build();

        when(mockTodobinRepository.findById(1L)).thenReturn(Optional.ofNullable(existingTodo));
        when(mockTodobinRepository.save(expected)).thenReturn(expected);

        Todo result = todobinService.patchTodo(1L, updatedTodo);

        assertThat(result).isEqualTo(expected);
        verify(mockTodobinRepository).save(expected);
    }

    @Test
    public void getTodo_throwsTodoNotFoundException_ifTodoDoesNotExist() {
        thrown.expect(TodoNotFoundException.class);
        thrown.expectMessage("Todo with ID '1' not found.");

        when(mockTodobinRepository.findById(1L)).thenReturn(Optional.empty());

        todobinService.getTodo(1L);
    }
}
