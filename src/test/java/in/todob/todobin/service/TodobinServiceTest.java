package in.todob.todobin.service;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.exception.BadRequest;
import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.repository.TodobinRepository;
import in.todob.todobin.util.ShortIdMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodobinServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private TodobinRepository mockTodobinRepository;

    @Mock
    private TodolistService mockTodolistService;

    private TodobinService todobinService;

    @Before
    public void setup() {
        todobinService = new TodobinService(mockTodobinRepository, mockTodolistService);

        when(mockTodolistService.getTodolist(eq("u")))
                .thenReturn(Todolist.builder()
                                    .todos(Arrays.asList(Todo.builder().id(3022L).build()))
                                    .build());
    }

    @Test
    public void createTodo_persistsTodo_whenPassedTodo() {
        TodoRequest todoRequest = new TodoRequest();
            todoRequest.setTitle("A todo");
            todoRequest.setNotes("A todo description");

        when(mockTodobinRepository.save(any(Todo.class))).thenReturn(Todo.builder()
                                                                         .id(1L)
                                                                         .title("A todo")
                                                                         .notes("A todo description")
                                                                         .build());

        Todo result = todobinService.createTodo("u", todoRequest);

        assertThat(result.getTitle()).isEqualTo("A todo");
        assertThat(result.getNotes()).isEqualTo("A todo description");
        assertThat(result.getShortId()).isEqualTo(ShortIdMapper.encode(result.getId()));
    }

    @Test
    public void deleteTodo_deletesTodo_whenPassedValidTodoId() {
        when(mockTodobinRepository.findById(3022L)).thenReturn(Optional.ofNullable(Todo.builder().build()));

        todobinService.deleteTodo("u", "2x");

        verify(mockTodobinRepository).deleteById(3022L);
    }

    @Test
    public void deleteTodo_throwsTodoNotFoundException_whenTodoIdForDeletionDoesNotExist() {
        thrown.expect(TodoNotFoundException.class);
        thrown.expectMessage("Todo with ID '4c' not found.");

        todobinService.deleteTodo("u", "4c");
    }

    @Test
    public void getTodo_returnsTodoIfExists() {
        Todo todo = Todo.builder()
                        .id(3022L)
                        .shortId("2x")
                        .title("A todo")
                        .notes("A todo description")
                        .build();

        when(mockTodobinRepository.findById(3022L)).thenReturn(Optional.ofNullable(todo));

        Todo result = todobinService.getTodo("u", "2x");

        assertThat(result).isEqualTo(todo);
    }

    @Test
    public void patchTodo_patchesTodo_whenPassedFieldsWithUpdatedValues() {
        Todo existingTodo = Todo.builder()
                                .id(3022L)
                                .shortId("2x")
                                .title("A todo")
                                .notes("A todo description")
                                .status(false)
                                .build();

        TodoRequest todoRequest = new TodoRequest();
            todoRequest.setTitle("An updated todo");
            todoRequest.setNotes("An updated todo description");
            todoRequest.setStatus(true);

        Todo expected = Todo.builder()
                            .id(3022L)
                            .shortId("2x")
                            .title("An updated todo")
                            .notes("An updated todo description")
                            .status(true)
                            .build();

        when(mockTodobinRepository.findById(3022L)).thenReturn(Optional.ofNullable(existingTodo));
        when(mockTodobinRepository.save(expected)).thenReturn(expected);

        Todo result = todobinService.patchTodo("u", "2x", todoRequest);

        assertThat(result).isEqualTo(expected);
        verify(mockTodobinRepository).save(expected);
    }

    @Test
    public void createTodo_throwsInvalidRequest_whenTodoRequestBodyMissingRequiredFields() {
        thrown.expect(BadRequest.class);
        thrown.expectMessage("Request body malformed.");

        TodoRequest todoRequest = new TodoRequest();

        todobinService.createTodo("u", todoRequest);
    }

    @Test
    public void getTodo_throwsTodoNotFoundException_ifTodoDoesNotExist() {
        thrown.expect(TodoNotFoundException.class);
        thrown.expectMessage("Todo with ID '3c' not found.");

        todobinService.getTodo("u", "3c");
    }

    @Test
    public void patchTodo_throwsTodoNotFoundException_ifTodoDoesNotExist() {
        thrown.expect(TodoNotFoundException.class);
        thrown.expectMessage("Todo with ID '5c' not found.");

        todobinService.patchTodo("u","5c", new TodoRequest());
    }
}
